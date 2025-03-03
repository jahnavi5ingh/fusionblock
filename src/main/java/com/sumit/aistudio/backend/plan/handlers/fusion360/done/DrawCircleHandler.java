package com.sumit.aistudio.backend.plan.handlers.fusion360.done;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.models.Point;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@IsNodeHandler
@Component
public class DrawCircleHandler extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
        Object input = node.getData().getProperty("point");
        if (input instanceof Point) {
            String name=  node.getData().getStringProperty("name");
            if(isEmpty(name)){
                name="c_"+ RandomStringUtils.randomAlphabetic(5);
            }
            Point point = (Point) node.getData().getProperty("point");
            float radius=  node.getData().getFloatProperty("radius");
            try {
                System.out.println(point);
                fusion360Client.drawCircle(pointToFloatArray(point),radius,name);

                node.getOutput().getProperties().put("output", name);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (input instanceof List){
            List<Point> points = (List<Point>) node.getData().getProperty("point");
            String name=  node.getData().getStringProperty("name");
            if(isEmpty(name)){
                name="c_"+ RandomStringUtils.randomAlphabetic(5);
            }
            float radius=  node.getData().getFloatProperty("radius");
            try {
                int i = 0;
                for(Point point: points){
                    fusion360Client.drawCircle(pointToFloatArray(point),radius,name+"_"+i++);
                }
                node.getOutput().getProperties().put("output", name);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }


    }
}
