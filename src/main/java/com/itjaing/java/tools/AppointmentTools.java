package com.itjaing.java.tools;

import com.alibaba.fastjson2.JSONObject;
import com.itjaing.java.entity.Appointment;
import com.itjaing.java.service.AppointmentService;
import dev.langchain4j.agent.tool.Tool;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * package com.itjaing.java.tools
 * author 江小康
 * date 2026/4/1 22:41
 * description
 */
@Component
public class AppointmentTools {

    @Autowired
    AppointmentService appointmentService;


    //预约挂号
    @Tool(name="预约挂号", value = "根据参数，先执行工具方法querySchedule查询是否可预约，并直接给" +
            "用户回答是否可预约，并让用户确认所有预约信息，用户确认后再进行预约。如果用户没有提供具体的医生姓名，请从向量存储中找到一位医生")
    public String bookAppointment(Appointment appointment) {

        Appointment appointmentDB = appointmentService.getOne(appointment);
        if(appointmentDB==null){
            appointment.setId(null); //预约时，没有id值的。设置为null。否则可能给大模型造成幻觉。
            boolean isSaveOk = appointmentService.save(appointment);
            if(isSaveOk){
                return "预约成功，并返回预约详情";
            }else{
                return "预约失败";
            }
        }
        return "您在相同的科室和时间已有预约";
    }

    //取消预约挂号
    @Tool(name = "取消预约挂号",value = "根据参数，查询预约是否存在，如果存在则删除预约记录并返回取消预约成功，否则返回取消预约失败")
    public String cancelAppointment(Appointment appointment) {
        Appointment appointmentDB = appointmentService.getOne(appointment);
        if(appointmentDB!=null){
            boolean isDeleteOk = appointmentService.removeById(appointmentDB.getId());
            if(isDeleteOk){
                return "取消预约成功";
            }else{
                return "取消预约失败";
            }
        }
        return "您没有预约记录，请核对预约科室和时间";
    }


    //查询是否可以预约挂号
    @Tool(name = "查询是否有号源",value = "根据科室名称，日期，时间和医生查询是否有号源，并返回给用户")
    public boolean querySchedule(Appointment appointment){
        //远程调用service_hosp微服务接口，查询医生排班号源，判断是否可以进行预约。

        //参考 短信发送。通过HttpClient远程调用短信服务接口代码。


        String name = appointment.getDepartment(); //科室名称
        String doctorName = appointment.getDoctorName(); //医生名称
        String date = appointment.getDate(); //预约日期
        String time = appointment.getTime(); //预约时间

        String host = "http://localhost:8201";
        String path = "/api/hosp/hospital/selectSchedule";
        String method = "POST";

        Map<String, String> headers = new HashMap<String, String>();

        //根据API的要求，定义相对应的Content-Type
        //headers.put("Content-Type", "application/json; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();

        bodys.put("name", name);
        bodys.put("doctorName", doctorName);
        bodys.put("date", date);
        bodys.put("time", time);

        try {
            //利用HttpClient进行远程调用
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity,"UTF-8");
            System.out.println(result);
            JSONObject jsonObject = JSONObject.parseObject(result);
            if(jsonObject.getInteger("code") == 200){
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}

