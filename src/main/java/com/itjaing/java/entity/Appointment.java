package com.itjaing.java.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * package com.itjaing.java.entity
 * author 江小康
 * date 2026/4/1 22:04
 * description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {
    @TableId(type = IdType.AUTO)
    @Schema(description = "预约编号")
    private Long id;
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "身份证号")
    private String idCard;
    @Schema(description = "预约科室")
    private String department;
    @Schema(description = "预约日期")
    private String date;
    @Schema(description = "预约时间")
    private String time;
    @Schema(description = "预约医生")
    private String doctorName;
}
