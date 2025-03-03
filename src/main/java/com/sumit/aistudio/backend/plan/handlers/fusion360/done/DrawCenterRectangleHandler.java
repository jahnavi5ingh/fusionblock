package com.sumit.aistudio.backend.plan.handlers.fusion360.done;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.models.Point;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@IsNodeHandler
@Component
public class DrawCenterRectangleHandler extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
        String name=  node.getData().getStringProperty("name");
        if(isEmpty(name)){
            name="c_"+ RandomStringUtils.randomAlphabetic(5);
        }
        Point point = (Point) node.getData().getProperty("point");
       float width=  node.getData().getFloatProperty("width");
         float height=  node.getData().getFloatProperty("height");
        try {
            System.out.println(name);
            fusion360Client.drawCenteredRectangle(pointToFloatArray(point),width,height,name);

            node.getOutput().getProperties().put("output", name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
