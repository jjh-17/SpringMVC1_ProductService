package springMVC.productservice.web.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springMVC.productservice.domain.Product;
import springMVC.productservice.domain.ProductRepository;

import java.util.List;

//상품 목록 컨트롤러
@Controller
@RequestMapping("/basic/products")
@RequiredArgsConstructor
public class BasicProductController {

    private final ProductRepository productRepository;

    //모든 상품 리스트
    @GetMapping
    public String products(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "basic/products";
    }

    //상품 상세 정보
    @GetMapping("/{id}")
    public String product(@PathVariable long id, Model model) {
        Product product = productRepository.findById(id);
        model.addAttribute("product", product);
        return "basic/product";
    }

    //상품 등록 폼 출력
    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

    //상품 등록 - @RequestParam
    //@PostMapping("/add")
    public String addProduct1(@RequestParam String name,
                               @RequestParam int price,
                               @RequestParam Integer quantity,
                               Model model) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setQuantity(quantity);

        productRepository.save(product);
        model.addAttribute("product", product);

        return "basic/product";
    }

    //상품 등록 - @ModelAttribute : 객체 생성 및 addAttribute 수행
    //@PostMapping("/add")
    public String addProduct2(@ModelAttribute("product") Product product) {

        productRepository.save(product);

        return "basic/product";
    }

    //상품 등록 - @ModelAttribute : Model 미지정시, 클래스형의 앞부분을 소문자로 바꾼 Model에 addAttribute 수행
    //HelloData product면 addAttribute('helloData', product) 수행
    //@PostMapping("/add")
    public String addProduct3(@ModelAttribute Product product) {

        productRepository.save(product);

        return "basic/product";
    }

    //상품 등록 - @ModelAttribute 생략
    //String, int와 같은 형은 @RequestParam이, 사용자 정의 클래스 형인 경우엔 @ModelAttribute 자동 수행
    //@ModelAttribute 자동 수행
    @PostMapping("/add")
    public String addProduct4(Product product) {

        productRepository.save(product);

        return "basic/product";
    }

    //상품 수정 폼
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id);
        model.addAttribute("product", product);
        return "basic/editForm";
    }

    //상품 수정
    @PostMapping("/{id}/edit")
    public String editProduct(@PathVariable Long id, @ModelAttribute Product product) {
        productRepository.update(id, product);
        return "redirect:/basic/products/{id}";
    }

    //테스트용 데이터 추가
    @PostConstruct
    public void testInit() {
        productRepository.save(new Product("A", 10000, 10));
        productRepository.save(new Product("B", 20000, 20));
    }
}
