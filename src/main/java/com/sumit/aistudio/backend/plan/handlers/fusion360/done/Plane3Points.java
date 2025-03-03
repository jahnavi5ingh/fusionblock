package com.sumit.aistudio.backend.plan.handlers.fusion360.done;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.models.Point;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@IsNodeHandler
@Component
public class Plane3Points extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
          List<Point> pts = (List<Point>) node.getData().getProperty("points");
          List<float[]> points = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                points.add( pointToFloatArray(pts.get(i)));
            }
          try {
            fusion360Client.add3PointPlane(
                    node.getData().getStringProperty("name"),
                    points
                    );
            node.getOutput().getProperties().put("output", "done");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
