package com.itjaing.java.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itjaing.java.entity.Appointment;
import com.itjaing.java.mapper.AppointmentMapper;
import com.itjaing.java.service.AppointmentService;
import org.springframework.stereotype.Service;

/**
 * package com.itjaing.java.service.impl
 * author 江小康
 * date 2026/4/1 22:17
 * description
 */
@Service
public class AppointmentServiceImpl extends ServiceImpl<AppointmentMapper, Appointment> implements AppointmentService {
    @Override
    public Appointment getOne(Appointment appointment) {
        LambdaQueryWrapper<Appointment> queryWrapper = new LambdaQueryWrapper<>();
       queryWrapper.eq(Appointment::getId, appointment.getId());
       queryWrapper.eq(Appointment::getUsername, appointment.getUsername());
       queryWrapper.eq(Appointment::getIdCard, appointment.getIdCard());
       queryWrapper.eq(Appointment::getDepartment, appointment.getDepartment());
       queryWrapper.eq(Appointment::getDate, appointment.getDate());
       queryWrapper.eq(Appointment::getTime, appointment.getTime());
       queryWrapper.eq(Appointment::getDoctorName, appointment.getDoctorName());
       return baseMapper.selectOne(queryWrapper);
    }
}
