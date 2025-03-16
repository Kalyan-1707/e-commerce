package com.ecommerce.project.service;

import java.io.IOException;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.repository.CategoryRepository;
import com.ecommerce.project.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);
        Product productFromDB = productRepository.findByProductNameIgnoreCase(product.getProductName());

        if(productFromDB != null){
            throw new APIException("Product already exist");
        }

        Category category = categoryRepository.findById(categoryId)
                            .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));

        
        product.setCategory(category);
        product.setImage("default.png");
        double specialPrice = product.getPrice() - product.getPrice()*(product.getDiscount()/100);
        product.setSpecialPrice(specialPrice);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productPage = productRepository.findAll(pageDetails);

        List<Product> products = productPage.getContent();

        List<ProductDTO> productDTOs = products.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();

        ProductResponse productResponse  =  new ProductResponse();
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setLastPage(productPage.isLast());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalPages(productPage.getTotalPages());

        productResponse.setContent(productDTOs);

        return productResponse;

    }

    @Override
    public ProductResponse getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category category = categoryRepository.findById(categoryId)
        .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productPage = productRepository.findByCategoryOrderByPriceAsc(category, pageDetails);

        List<Product> products = productPage.getContent();

        List<ProductDTO> productDTOs = products.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();

        ProductResponse productResponse  =  new ProductResponse();
        productResponse.setContent(productDTOs);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setLastPage(productPage.isLast());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalPages(productPage.getTotalPages());


        return productResponse;
    }

    @Override
    public ProductResponse getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productPage = productRepository.findByProductNameContainingIgnoreCase(keyword, pageDetails);

        List<Product> products = productPage.getContent();

        List<ProductDTO> productDTOs = products.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();

        ProductResponse productResponse  =  new ProductResponse();
        productResponse.setContent(productDTOs);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setLastPage(productPage.isLast());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalPages(productPage.getTotalPages());

        return productResponse;   
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product productFromDB = productRepository.findById(productId)
        .orElseThrow(() -> new ResourceNotFoundException("Product","productId",productId));

        Product product = modelMapper.map(productDTO, Product.class);
        productFromDB.setProductName(product.getProductName());
        productFromDB.setDescription(product.getDescription());
        productFromDB.setDiscount(product.getDiscount());
        productFromDB.setImage(product.getImage());
        productFromDB.setPrice(product.getPrice());
        productFromDB.setQuantity(product.getQuantity());
        double specialPrice = product.getPrice() - product.getPrice()*(product.getDiscount()/100);
        productFromDB.setSpecialPrice(specialPrice);

        productRepository.save(productFromDB);

        return modelMapper.map(productFromDB, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product productFromDB = productRepository.findById(productId)
        .orElseThrow(() -> new ResourceNotFoundException("Product","productId",productId));

        productRepository.delete(productFromDB);

        return modelMapper.map(productFromDB, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product productFromDB = productRepository.findById(productId)
        .orElseThrow(() -> new ResourceNotFoundException("Product","productId",productId));

        
        String fileName = fileService.upload(path, image);
        
        productFromDB.setImage(fileName);
        productRepository.save(productFromDB);
        
        return modelMapper.map(productFromDB, ProductDTO.class);
        }
        
}
