package springMVC.productservice.web.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
    //@PostMapping("/add")
    public String addProduct4(Product product) {

        productRepository.save(product);

        return "basic/product";
    }

    /*
    addProduct1~4 : 상품 추가 후, 새로고침 시 상품이 무한히 저장됨
        ==> 등록 후 상태는 Post/add. 새로고침 시 마지막에 서버에 전송한 데이터를 다시 전송된다.
            즉, Post/add가 서버로 다시 전송되어 상품이 다시 저장된다
            ==> redirect를 이용하여 Get을 보내도록 한다.
     */
    //상품 추가 : redirect
    //@PostMapping("/add")
    public String addProduct5(Product product) {
        productRepository.save(product);
        return "redirect:/basic/products/" + product.getId();
    }

    //상품 추가 : RedirectAttributes : URL 인코딩 수행
    @PostMapping("/add")
    public String addProduct6(Product product, RedirectAttributes redirectAttributes) {
        Product savedProduct = productRepository.save(product);

        redirectAttributes.addAttribute("id", savedProduct.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/products/{id}";
    }


    //상품 수정 폼
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id);
        model.addAttribute("product", product);
        return "basic/editForm";
    }

    //상품 수정 - @PathVariable : @Mapping된 URI 내 'id'에 할당된 값을 갖는다
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
