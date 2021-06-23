package com.evision.dosage.service.vehicle;

import com.evision.dosage.pojo.entity.vehicle.VehicleSummaryDosageEntity;
import com.evision.dosage.pojo.model.DosageHeader;
import com.evision.dosage.service.vehicle.imp.VehicleSummaryDosageServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author DingZhanYang
 * @date 2020/2/25 15:55
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class VehicleSummaryDosageServiceTest {
    @Resource
    VehicleSummaryDosageServiceImpl service;

    @Test
    public void testGetHeader() throws Exception {
        List<DosageHeader> ret = service.getHeader();
        Assert.assertTrue(ret.size() > 0);
    }

    @Test
    public void testGetDataList() throws Exception {
        List<VehicleSummaryDosageEntity> ret = service.getDataList("", "", 1, 0);
        Assert.assertTrue(ret.size() > 0);
    }
}
