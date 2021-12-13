package com.springcloud.order.service.impl;

import com.springCloud.pojo.Payment;
import com.springcloud.order.dao.PaymentDao;
import com.springcloud.order.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentImple implements PaymentService {
    @Autowired
    PaymentDao paymentDao;
    @Override
    public int create(Payment payment) {
        return paymentDao.create(payment);
    }

    @Override
    public Payment queryById(long id) {
        return paymentDao.queryById(id);
    }
}
