package com.itjaing.java.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itjaing.java.entity.Appointment;

/**
 * package com.itjaing.java.service
 * author 江小康
 * date 2026/4/1 22:16
 * description
 */
public interface AppointmentService extends IService<Appointment> {
    Appointment getOne(Appointment appointment);
}
