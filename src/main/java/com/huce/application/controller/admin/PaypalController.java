package com.huce.application.controller.admin;

import com.huce.application.config.Constants;
import com.huce.application.config.PaypalPaymentIntent;
import com.huce.application.config.PaypalPaymentMethod;
import com.huce.application.entity.Order;
import com.huce.application.repository.OrderRepository;
import com.huce.application.service.OrderService;
import com.huce.application.service.PaypalService;
import com.huce.application.utils.PaypalUtil;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PaypalController {
    public static final String URL_PAYPAL_SUCCESS = "pay/success";
    public static final String URL_PAYPAL_CANCEL = "pay/cancel";
    public static long idOrder = 0;

    private Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private PaypalService paypalService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/pay")
    public String pay(HttpServletRequest request, @RequestParam(defaultValue = "0") double price, long id ){
        String cancelUrl = PaypalUtil.getBaseURL(request) + "/" + URL_PAYPAL_CANCEL;
        String successUrl = PaypalUtil.getBaseURL(request) + "/" + URL_PAYPAL_SUCCESS;
        idOrder = id;
        try {
            Payment payment = paypalService.createPayment(
                    price,
                    "USD",
                    PaypalPaymentMethod.paypal,
                    PaypalPaymentIntent.sale,
                    "payment description",
                    cancelUrl,
                    successUrl,id);
            for(Links links : payment.getLinks()){
                if(links.getRel().equals("approval_url")){
                    return "redirect:" + links.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:";
    }
    @GetMapping(URL_PAYPAL_CANCEL)
    public String cancelPay(){
        return "redirect:/tai-khoan/lich-su-giao-dich/" ;
    }
    @GetMapping(URL_PAYPAL_SUCCESS)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId){
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            Order order = orderService.findOrderById(idOrder);
            order.setStatus(Constants.PAYMENT_STATUS);
            orderRepository.save(order);
            if(payment.getState().equals("approved")){
                return "redirect:/tai-khoan/lich-su-giao-dich/";
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:/tai-khoan/lich-su-giao-dich/";
    }
}
