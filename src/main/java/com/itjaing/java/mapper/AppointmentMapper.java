package com.itjaing.java.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itjaing.java.entity.Appointment;
import org.apache.ibatis.annotations.Mapper;

/**
 * package com.itjaing.java.mapper
 * author 江小康
 * date 2026/4/1 22:06
 * description
 */
@Mapper
public interface AppointmentMapper extends BaseMapper<Appointment> {
}
