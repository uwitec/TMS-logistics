package com.yydscm.Controller.arrival;

import com.yydscm.Enum.ResultEnum;
import com.yydscm.Service.Billing.BillingService;
import com.yydscm.Service.arrival.ArrivalService;
import com.yydscm.Util.HsUtil;
import com.yydscm.Util.ResultUtil;
import com.yydscm.Utils.RedisUtil;
import com.yydscm.view.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

/**
 * 到货
 *
 * @author HOME_HCL
 */
@RestController
@RequestMapping("/arrival")
public class ArrivalController {

    @Autowired
    ArrivalService arrivalService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    BillingService billingService;


    /**
     * 批量到货
     *
     * @param map
     * @return
     */
    @RequestMapping("/updateBillingForArrival")
    public Result<?> updateBillingOFInvoiceStatus(@RequestParam Map<String, Object> map, HttpServletRequest request) {
        if (Objects.isNull(map) || map.entrySet().isEmpty()) {
            return ResultUtil.error(ResultEnum.FAIL_SAVE.getCode(), ResultEnum.FAIL_SAVE.getMsg());
        }
        map = Objects.isNull(HsUtil.noAttribute("map", map)) ? map : HsUtil.toHashMap(map.get("map"));
        //在session中取得用户所属网点信息
        Map<String, Object> user = redisUtil.getUser((String) request.getAttribute("userid"));
        Long point_uuid = (Long) user.get("company_uuid");
        Long operator_uuid = (Long) user.get("uuid");
        map.put("point_uuid", point_uuid);
        map.put("operator_uuid", operator_uuid);
        return arrivalService.updateBillingOFInvoiceStatus(map);
    }

    /**
     * 到货列表
     *
     * @param map
     * @return
     */
    @PostMapping("/selectArrivalList")
    public Result<?> selectArrivalList(@RequestParam Map<String, Object> map, HttpServletRequest request) {
        map = Objects.isNull(HsUtil.noAttribute("map", map)) ? map : HsUtil.toHashMap(map.get("map"));
        Map<String, Object> user = redisUtil.getUser((String) request.getAttribute("userid"));
        Long point_uuid = (Long) user.get("company_uuid");
        map.put("terminal_station_id", point_uuid);
        map.put("invoice_status", 3);
        return billingService.selectBilling(map);
    }

}
