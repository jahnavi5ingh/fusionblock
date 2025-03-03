package com.sumit.aistudio.backend;

public class PlanExecutor {
    public static void main(String[] args) {
        String jsonString = "{\"nodes\":[{\"id\":\"d_2534ea0e-a173-4cf9-a9c8-5705d9fa97a6\",\"type\":\"PromptNode\",\"position\":{\"x\":169,\"y\":-356.5},\"data\":{\"label\":\"PromptNode node\",\"pname\":\"b\",\"promptText\":\"asdaads\\nasd\\nas\\ndassd\\naqasdasd\"},\"measured\":{\"width\":379,\"height\":269},\"selected\":true,\"dragging\":false,\"width\":379,\"height\":269,\"resizing\":false},{\"id\":\"d_c84ab02c-48d5-40b1-b1f5-6adf4bce3a6c\",\"type\":\"PromptNode\",\"position\":{\"x\":-355,\"y\":-428.3125},\"data\":{\"label\":\"PromptNode node\",\"pname\":\"c\",\"promptText\":\"cccccc\"},\"measured\":{\"width\":355,\"height\":183},\"selected\":false,\"dragging\":false,\"width\":355,\"height\":183,\"resizing\":false}],\"edges\":[{\"source\":\"d_c84ab02c-48d5-40b1-b1f5-6adf4bce3a6c\",\"sourceHandle\":\"b\",\"target\":\"d_2534ea0e-a173-4cf9-a9c8-5705d9fa97a6\",\"id\":\"xy-edge__d_c84ab02c-48d5-40b1-b1f5-6adf4bce3a6cb-d_2534ea0e-a173-4cf9-a9c8-5705d9fa97a6\"},{\"source\":\"d_c84ab02c-48d5-40b1-b1f5-6adf4bce3a6c\",\"sourceHandle\":\"output1\",\"target\":\"d_2534ea0e-a173-4cf9-a9c8-5705d9fa97a6\",\"targetHandle\":\"input1\",\"id\":\"xy-edge__d_c84ab02c-48d5-40b1-b1f5-6adf4bce3a6coutput1-d_2534ea0e-a173-4cf9-a9c8-5705d9fa97a6input1\"}],\"viewport\":{\"x\":218.16905512340773,\"y\":345.0341203415856,\"zoom\":0.5}}";
        com.sumit.aistudio.backend.plan.PlanExecutor planExecutor = new com.sumit.aistudio.backend.plan.PlanExecutor();
        planExecutor.executeGraph(jsonString);
    }
}
