package com.shop.controller;

import com.shop.constant.GiftStatus;
import com.shop.dto.OrderDto;
import com.shop.dto.OrderHistDto;
import com.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 주문
    @PostMapping(value = "/order")
    public @ResponseBody
    ResponseEntity order(@RequestBody @Valid OrderDto orderDto,
                         BindingResult bindingResult, Principal principal) {

        if (bindingResult.hasErrors()) {                // orderDTO 객체에 데이터 바인딩시 에러 검사
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(),
                    HttpStatus.BAD_REQUEST);           // 에러 정보를 ResponseEntity 객체에 담아서 반환
        }

        String email = principal.getName();           // principal 객체에서 현재 로그인한 회원의 이메일 정보 조회
        Long orderId;

        try {
            orderId = orderService.order(orderDto, email);          // 주문 로직 호출
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(orderId, HttpStatus.OK);    // HTTP 응답 상태 코드 반환
    }

    // 전체 조회
    @GetMapping(value = {"/orders", "/orders/{page}"})
    public String orderHist(@PathVariable("page") Optional<Integer> page,
                            Principal principal, Model model) {

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);
        Page<OrderHistDto> ordersHistDtoList;

        ordersHistDtoList =
                orderService.getOrderList(principal.getName(), pageable);

        model.addAttribute("orders", ordersHistDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);

        return "order/orderHist";
    }

    // 구매/선물 상태 조회
    @GetMapping(value = {"/ordersStatus/{status}", "/ordersStatus/{page}"})
    public String orderStatus(@PathVariable("page") Optional<Integer> page,
                              @PathVariable(required = false, value="status") GiftStatus giftStatus,
                              Principal principal, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);
        Page<OrderHistDto> ordersHistDtoList;

        ordersHistDtoList =
                orderService.getOrderListStatus(principal.getName(), pageable, giftStatus);

        model.addAttribute("orders", ordersHistDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);
        return "order/orderHist";
    }


    // 주문 취소
    @PostMapping("/order/{orderId}/cancel")
    public @ResponseBody
    ResponseEntity cancelOrder(@PathVariable("orderId") Long orderId, Principal principal) {

        // 주문 취소 권한 검사
        if (!orderService.validateOrder(orderId, principal.getName())) {
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        // 주문 취소 로직 호출
        orderService.cancelOrder(orderId);
        System.out.println(orderId);
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

}
