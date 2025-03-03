package com.sumit.aistudio.backend.plan.handlers.fusion360.done;

import com.sumit.aistudio.backend.graph.Node;
import com.sumit.aistudio.backend.models.Path;
import com.sumit.aistudio.backend.models.Point;
import com.sumit.aistudio.backend.plan.IsNodeHandler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@IsNodeHandler
@Component
public class DrawCurve extends Fusion360Handler {

    @Override
    public void handleNode(Node node) {
        String name = node.getData().getStringProperty("name");
        boolean close = node.getData().getBooleanProperty("close");
        Object crv =  node.getData().getProperty("curve");
        List<Point> points = null;
        if(crv instanceof String){
            points = (List<Point>) stringToListPoint((String) crv);
        }else{
            points = (List<Point>) crv;
        }

        if(close){
            //add the first point to the last path
           points.add(new Point(points.get(0)));
        }

        try {
            System.out.println(points);
            List<Path> paths = new ArrayList<>();
            //iterate throgh points
            //create path object add points to it and add path to paths
            //if the current points mode is not same as last point mode craete a new path and add previous to paths
            //add the last path to paths
            //set the mode of path as first point in iteration
            for(int i = 0; i < points.size(); i++) {
                Point point = points.get(i);
                if(i == 0) {
                    Path path = new Path();
                    path.setMode(getMode(point));
                    path.addPoint(point);
                    paths.add(path);
                } else {
                    Path path = paths.get(paths.size() - 1);
                    if(!path.getMode().equals(getMode(point))) {
                        path = new Path();
                        path.setMode(getMode(point));
                        paths.add(path);
                    }
                    path.addPoint(point);
                }
            }

            //iterate over paths and add a connecting path between paths
            Object planeVar = node.getExecutionContext().getVars().get("selected_plane");
            planeVar = planeVar == null ? "xy" : planeVar;
            String selected_plane = planeVar.toString();
            if(selected_plane.equals("xz")) {
                //for each path iterate its points and sway y and z
                for(Path path: paths) {
                    for(Point point: path.getPoints()) {
                        point.setY(point.getY()*-1);


                    }
                }
            }
            List<Path>paths1 = new ArrayList<>();
            for(int i = 0; i < paths.size(); i++) {
                Path path = paths.get(i);
                if(i != paths.size() - 1) {
                    Path nextPath = paths.get(i + 1);
                    {
                        Path connectingPath = new Path();
                        connectingPath.setMode("Line");
                        connectingPath.addPoint(path.getPoints().get(path.getPoints().size() - 1));
                        connectingPath.addPoint(nextPath.getPoints().get(0));
                        paths1.add(connectingPath);
                    }
                }
            }

            paths.addAll(paths1);

            fusion360Client.drawCurves(paths,  name);

            node.getOutput().getProperties().put("output", "done");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private static String getMode(Point point) {
        if((point.getMode() != null && point.getMode().toLowerCase().equals("line")) || point.getTags().contains("Line")|| point.getTags().contains("line")) {
            return "Line";
        }else if((point.getMode() != null && point.getMode().toLowerCase().equals("spline")) || point.getTags().contains("Spline") || point.getTags().contains("spline")) {
            return "Spline";
        } else{
            return "Line";
        }

    }


}
