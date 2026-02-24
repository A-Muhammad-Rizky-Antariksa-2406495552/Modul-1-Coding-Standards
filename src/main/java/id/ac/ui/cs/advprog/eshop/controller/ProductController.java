package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    private static final String CREATE_PRODUCT_VIEW = "createProduct";
    private static final String EDIT_PRODUCT_VIEW = "editProduct";
    private static final String PRODUCT_LIST_VIEW = "productList";
    private static final String REDIRECT_PRODUCT_LIST = "redirect:/product/list";
    private static final String ERROR_ATTRIBUTE = "error";
    private static final String PRODUCT_NAME_REQUIRED = "Product name is required";
    private static final String QUANTITY_POSITIVE = "Quantity must be positive";
    private static final String PRODUCT_ATTRIBUTE = "product";
    private static final String PRODUCTS_ATTRIBUTE = "products";

    @Autowired
    private ProductService service;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute(PRODUCTS_ATTRIBUTE, service.findAll());
        return PRODUCT_LIST_VIEW;
    }

    @GetMapping("/create")
    public String createProductPage(Model model) {
        Product product = new Product();
        model.addAttribute(PRODUCT_ATTRIBUTE, product);
        return CREATE_PRODUCT_VIEW;
    }

    @PostMapping("/create")
    public String createProductPost(@ModelAttribute Product product, Model model) {
        String validationError = validateProduct(product);
        if (validationError != null) {
            model.addAttribute(ERROR_ATTRIBUTE, validationError);
            return CREATE_PRODUCT_VIEW;
        }

        service.create(product);
        return REDIRECT_PRODUCT_LIST;
    }

    @GetMapping("/list")
    public String productListPage(Model model) {
        List<Product> allProducts = service.findAll();
        model.addAttribute(PRODUCTS_ATTRIBUTE, allProducts);
        return PRODUCT_LIST_VIEW;
    }

    @GetMapping("/edit/{id}")
    public String editProductPage(@PathVariable("id") String id, Model model) {
        Product product = service.findById(id);
        if (product == null) {
            return REDIRECT_PRODUCT_LIST;
        }
        model.addAttribute(PRODUCT_ATTRIBUTE, product);
        return EDIT_PRODUCT_VIEW;
    }

    @PostMapping("/edit")
    public String editProductPost(@ModelAttribute Product product, Model model) {
        String validationError = validateProduct(product);
        if (validationError != null) {
            model.addAttribute(PRODUCT_ATTRIBUTE, product);
            model.addAttribute(ERROR_ATTRIBUTE, validationError);
            return EDIT_PRODUCT_VIEW;
        }

        service.update(product.getProductId(), product);
        return REDIRECT_PRODUCT_LIST;
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") String id) {
        Product product = service.findById(id);
        if (product != null) {
            service.delete(id);
        }
        return REDIRECT_PRODUCT_LIST;
    }

    private String validateProduct(Product product) {
        if (product.getProductName() == null || product.getProductName().trim().isEmpty()) {
            return PRODUCT_NAME_REQUIRED;
        }
        if (product.getProductQuantity() < 0) {
            return QUANTITY_POSITIVE;
        }
        return null;
    }
}