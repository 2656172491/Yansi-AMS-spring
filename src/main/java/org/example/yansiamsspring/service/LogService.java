package org.example.yansiamsspring.service;

import org.example.yansiamsspring.mapper.AssetFlowLogMapper;
import org.example.yansiamsspring.mapper.LoginLogMapper;
import org.example.yansiamsspring.mapper.OperationLogMapper;
import org.example.yansiamsspring.pojo.AssetFlowLog;
import org.example.yansiamsspring.pojo.LoginLog;
import org.example.yansiamsspring.pojo.OperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LogService {

    @Autowired
    private LoginLogMapper loginLogMapper;

    @Autowired
    private OperationLogMapper operationLogMapper;

    @Autowired
    private AssetFlowLogMapper assetFlowLogMapper;

    public void recordLogin(Long userId, String username, String name, String ip, String userAgent, boolean success, String failReason) {
        LoginLog log = new LoginLog();
        log.setUserId(userId);
        log.setUsername(username);
        log.setName(name);
        log.setIp(ip);
        log.setUserAgent(userAgent);
        log.setStatus(success ? 1 : 0);
        log.setFailReason(failReason);
        loginLogMapper.insert(log);
    }

    public void recordOperation(Long userId, String username, String name, String operationType,
                                String targetType, Long targetId, String detailText, boolean success, String ip) {
        OperationLog log = new OperationLog();
        log.setUserId(userId);
        log.setUsername(username);
        log.setName(name);
        log.setOperationType(operationType);
        log.setTargetType(targetType);
        log.setTargetId(targetId);
        log.setDetailText(detailText);
        log.setResult(success ? 1 : 0);
        log.setIp(ip);
        operationLogMapper.insert(log);
    }

    public void recordFlow(Long assetId, String assetType, String flowType, int quantity, String batchNo, String operator, Long operatorId) {
        AssetFlowLog log = new AssetFlowLog();
        log.setAssetId(assetId);
        log.setAssetType(assetType);
        log.setFlowType(flowType);
        log.setQuantity(quantity);
        log.setBatchNo(batchNo);
        log.setOperator(operator);
        log.setOperatorId(operatorId);
        assetFlowLogMapper.insert(log);
    }

    public List<LoginLog> findLoginLogs(String username, Integer status) {
        return loginLogMapper.findList(username, status);
    }

    public List<OperationLog> findOperationLogs(String operationType, String targetType) {
        return operationLogMapper.findList(operationType, targetType);
    }

    public List<Map<String, Object>> getFlowStats(int days) {
        return assetFlowLogMapper.countByDays(days);
    }
}
