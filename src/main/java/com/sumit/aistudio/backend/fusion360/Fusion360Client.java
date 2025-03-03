package com.sumit.aistudio.backend.fusion360;

import com.google.gson.Gson;
import com.sumit.aistudio.backend.models.Path;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.List;

public class Fusion360Client {
    public static final String SERVER_URL = "http://localhost:8001";
    public static final Gson gson = new Gson();
    public static final HttpClient client = HttpClient.newHttpClient();


    public Fusion360Client() {

    }



    public  void addText(String text, String targetPlane, float[] pts1, float[] pts22,float fontSize) throws Exception {
        Map<String, Object> params19 = new HashMap<>();
        params19.put("text",text);
        params19.put("target_plane", targetPlane);
        params19.put("position1",pts1);
        params19.put("position2", pts22);
        params19.put("font_size",fontSize);
        sendRequest("add_text", params19);
    }

    public  void renameBody(String from, String to) throws Exception {
        Map<String, Object> params18 = new HashMap<>();
        params18.put("current_name", from);
        params18.put("new_name",to);
        sendRequest("rename_body", params18);
    }

    public  void hideAllBodies() throws Exception {
        Map<String, Object> params17 = new HashMap<>();
        sendRequest("hide_all_bodies", params17);
    }

    public  void toggleSketchVisibility(String sketchName) throws Exception {
        Map<String, Object> params16 = new HashMap<>();
        params16.put("sketch_name", sketchName);
        sendRequest("toggle_sketch_visibility", params16);
    }

    public  void hideAllSketches() throws Exception {
        Map<String, Object> params15 = new HashMap<>();
        sendRequest("hide_all_sketches", params15);
    }

    public  void sweepSketch(String profileSketchName, String pathSketchName, String sweepName, float rotationAngle,int orientation) throws Exception {
        Map<String, Object> params14 = new HashMap<>();
        params14.put("profile_sketch", profileSketchName);
        params14.put("path_sketch", pathSketchName);
        params14.put("name", sweepName);
        params14.put("rotation_angle", rotationAngle);
        params14.put("orientation", orientation);
        sendRequest("sweep_sketch_along_path", params14);
    }

    public  void loftThroughSketches(String[] sketchNamesArray2, String name) throws Exception {
        Map<String, Object> params12 = new HashMap<>();
        params12.put("sketches", sketchNamesArray2);
        params12.put("name", name);
        sendRequest("loft_through_sketches", params12);
    }

    public  void loftBetweenSketches(String sketch1, String sketch2, String name) throws Exception {
        Map<String, Object> params11 = new HashMap<>();
        params11.put("sketch1", sketch1);
        params11.put("sketch2",sketch2);
        params11.put("name", name);
        sendRequest("loft_between_sketches", params11);
    }

    public  void joinBodies(String[] bodyNamesArrayJoin, String resultBodyName) throws Exception {
        Map<String, Object> params10 = new HashMap<>();
        params10.put("bodies", bodyNamesArrayJoin);
        params10.put("name", resultBodyName);
        sendRequest("join_bodies", params10);
    }

    public  void add3PointPlane(String name, List<float[]> ptsData) throws Exception {
        Map<String, Object> params8 = new HashMap<>();
        params8.put("name", name);
        params8.put("points", ptsData);
        sendRequest("add_plane_through_points", params8);
    }

    public  void showBody(String bodyName8) throws Exception {
        Map<String, Object> params7 = new HashMap<>();
        params7.put("body", bodyName8);
        sendRequest("show", params7);
    }

    public  void hideBody(String bodyName7) throws Exception {
        Map<String, Object> params6 = new HashMap<>();
        params6.put("body", bodyName7);
        sendRequest("hide", params6);
    }

    public  void scaleBody(String bodyName6, float scale) throws Exception {
        Map<String, Object> params5 = new HashMap<>();
        params5.put("body", bodyName6);
        params5.put("scale_factor", scale);
        sendRequest("scale", params5);
    }

    public  void rotateBody(String bodyName4, String axis1, float degs) throws Exception {
        Map<String, Object> params4 = new HashMap<>();
        params4.put("body", bodyName4);
        params4.put("axis", axis1);
        params4.put("angle", degs);
        sendRequest("rotate", params4);
    }

    public  void cutBody(String body1, String bodyTool) throws Exception {
        Map<String, Object> params3 = new HashMap<>();
        params3.put("body1", body1);
        params3.put("body2",bodyTool);
        sendRequest("cut", params3);
    }

    public  void moveBody(String bodyNameMove,float[] pts2) throws Exception {
        Map<String, Object> params2 = new HashMap<>();
        params2.put("body", bodyNameMove);

        params2.put("x", pts2[0]);
        params2.put("y",  pts2[1]);
        params2.put("z", pts2[2]);
        sendRequest("move", params2);
    }

    public  void duplicateBody(String bodyName, float x, float y, float z) throws Exception {
        Map<String, Object> params1 = new HashMap<>();
        params1.put("body", bodyName);
        params1.put("x", x);
        params1.put("y", y);
        params1.put("z", z);
        sendRequest("duplicate", params1);
    }

    public  void export(String fileName) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("filename", fileName);
        sendRequest("export", params);
    }

    public  void drawCircle(Scanner scanner, Fusion360Client fusionClient) throws Exception {
        //read pt and radius and name and call draw circle take all params in one line and take pt as : separated
        System.out.print("Enter pt (x, y, z), radius, and name: ");
        //read all in one line
        String allFields = scanner.nextLine();
        //parse and call draw circle pt is passed as 0:0:0, radius and name
        String[] fields = allFields.split(",");
        String ptString = fields[0];
        String[] pts = ptString.split(":");
        float cx = Float.parseFloat(pts[0]);
        float cy = Float.parseFloat(pts[1]);
        float cz = Float.parseFloat(pts[2]);
        float radius = Float.parseFloat(fields[1]);
        String circleName = fields[2];


        fusionClient.drawCircle(new float[]{cx, cy, cz}, radius, circleName);
    }


    public void drawLinesPair(float[][] points,float[][] points2, String name) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("points", points);
        params.put("points2", points2);
        params.put("name", name);
        sendRequest("draw_lines_pair", params);
    }
    public void drawLines(float[][] points, String name, boolean close) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("points", points);
        params.put("name", name);
        params.put("close", close);
        sendRequest("draw_lines", params);
    }
    public void drawFittedSpline(float[][] points, String name) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("points", points);
        params.put("name", name);
        sendRequest("draw_fitted_spline", params);
    }
    public void drawConicCurve(float[] startPoint, float[] endPoint, float[] apexPoint,float rhoValue, String name) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("start_point", startPoint);
        params.put("end_point", endPoint);
        params.put("apex_point", apexPoint);
        params.put("rho_value", rhoValue);
        params.put("name", name);
        sendRequest("draw_conic_curve", params);
    }

    public void draw3PtArc(float[] startPoint, float[] arcPoint, float[] endPoint, String name) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("start_point", startPoint);
        params.put("end_point", endPoint);
        params.put("point_on_arc", arcPoint);
        params.put("name", name);
        sendRequest("create_3_point_arc", params);
    }


    public void createSweepArc(float[] startPoint, float[] center_point,float sweepAngle, String name) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("start_point", startPoint);
        params.put("center_point", center_point);
        params.put("sweep_angle", sweepAngle);
        params.put("name", name);
        sendRequest("create_start_center_sweep_arc", params);
    }

    public void drawPoint(double x, double y, double z) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("x", x);
        params.put("y", y);
        params.put("z", z);
        sendRequest("draw_point", params);
    }
    public void drawPoints(float[][]  points) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("points", points);
        sendRequest("draw_points", params);
    }

    public void drawLine(double[] pt1, double[] pt2, String name) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("pt1", pt1);
        params.put("pt2", pt2);
        params.put("name", name);
        sendRequest("draw_line", params);
    }
    public void drawLine(float[] pt1, float[] pt2, String name) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("pt1", pt1);
        params.put("pt2", pt2);
        params.put("name", name);
        sendRequest("draw_line", params);
    }
    public void drawCircle(float[] pt1, float radius, String name) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("pt1", pt1);
        params.put("radius", radius);
        params.put("name", name);
        sendRequest("draw_circle", params);
    }

    public void drawRectangle(float[] pt1, float[] pt2, String name) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("pt1", pt1);
        params.put("pt2", pt2);
        params.put("name", name);
        sendRequest("draw_rectangle", params);
    }

    public void extrudeShape(String name, double offset, float angle,String bodyName) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("offset", offset);
        params.put("taperAngle", angle);
        params.put("bodyName", bodyName);

        sendRequest("extrude", params);
    }

    public void drawCenteredRectangle(float[] pt1, double width, double height, String name) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("pt1", pt1);
        params.put("width", width);
        params.put("height", height);
        params.put("name", name);
        sendRequest("draw_centered_rectangle", params);
    }

    public void addOffsetPlane(double z1, String name, String basePlane) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("offset", z1);
        params.put("name", name);
        params.put("base_plane", basePlane);
        sendRequest("add_offset_plane", params);
    }

    public void selectPlane(String name) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        sendRequest("select_plane", params);
    }

    public void quit() throws Exception {
        sendRequest("quit", new HashMap<>());
    }

    public void surfaceExtrude(String name, String profile, float distance, float taperAngle, int profileItemIndex ) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("profile_name", profile);
        params.put("taper_angle", taperAngle);
        params.put("distance", distance);
        params.put("item_index", profileItemIndex);
        sendRequest("surface_extrude", params);
    }
    public void surfaceRevolve(String name, String profile, String axis_name , float angle, int profileItemIndex ) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("profile_name", profile);
        params.put("angle", angle);
        params.put("axis_name", axis_name);
        params.put("item_index", profileItemIndex);
        sendRequest("surface_revolve", params);
    }
    public void surfaceSweep(String name, String profile, String pathName , float twistAngle,int orientation) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("profile_name", profile);
        params.put("path_name", pathName);
        params.put("twist_angle", twistAngle);
        params.put("orientation", orientation);
        sendRequest("surface_sweep", params);
    }

    public void surfaceLoft(String name, String []profiles) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("profile_names", profiles);
        sendRequest("surface_loft", params);
    }
    public void surfaceThicken(String name, String[] surfaceNames, float thickness) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("body_names", surfaceNames);
        params.put("thickness", thickness);
        sendRequest("thicken_surfaces", params);
    }

    public void surfacePatch(String name, String profile,int index) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("body_name", name);
        params.put("profile_name", profile);
        params.put("item_index", index);
        sendRequest("surface_patch", params);
    }

    public void createPipe(String path_name, float section_size,String section_type) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("path_name", path_name);
        params.put("section_size", section_size);
        params.put("section_type", section_type);
        sendRequest("create_pipe", params);
    }
    public void create_plane_along_path(String path_name, String name,float distance) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("path_name", path_name);
        params.put("name", name);
        params.put("distance", distance);
        sendRequest("create_plane_along_path", params);
    }
    public void create_n_planes_along_path(String path_name, String name,int n) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("path_name", path_name);
        params.put("name", name);
        params.put("n", n);
        sendRequest("create_n_planes_along_path", params);
    }
    public String create_point_along_path(String path_name, String name,float distance) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("path_name", path_name);
        params.put("name", name);
        params.put("distance", distance);
        return  sendRequest("create_point_along_path", params);
    }
    public String create_n_points_along_path(String path_name, String name,int n) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("path_name", path_name);
        params.put("name", name);
        params.put("n", n);
       return sendRequest("create_n_points_along_path", params);
    }
    public String create_tangentplane_along_path(String path_name, String name,float distance,int n,int dirx, int diry) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("path_name", path_name);
        params.put("name", name);
        params.put("distance", distance);
        params.put("dirx", dirx);
        params.put("diry", diry);
        params.put("n", n);
        return sendRequest("create_tangentplane_along_path", params);
    }
    /*surface commands*/
public void revolve(String name, int axis, float angle,String bodyName) throws Exception {
    Map<String, Object> params = new HashMap<>();
    params.put("name", name);
    params.put("axis_index", axis);
    params.put("angle", angle);
    params.put("bodyName", bodyName);

    sendRequest("revolve", params);
}
    public void drawCurves(List<Path> paths, String name)throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("paths", paths);
        params.put("count", paths.size());

        sendRequest("drawCurves", params);
    }


    public static String sendRequest(String command, Map<String, Object> params) throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("command", command);
        request.put("params", params);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI(SERVER_URL))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(gson.toJson(request)))
                .build();

        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        return response.body();

    }


}
