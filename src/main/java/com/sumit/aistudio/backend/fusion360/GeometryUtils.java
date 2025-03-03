package com.sumit.aistudio.backend.fusion360;

import com.sumit.aistudio.backend.models.Point;
import org.locationtech.jts.algorithm.ConvexHull;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.linearref.LengthIndexedLine;
import org.locationtech.jts.operation.buffer.BufferOp;
import org.locationtech.jts.operation.buffer.BufferParameters;
import org.locationtech.jts.operation.distance.DistanceOp;
import org.locationtech.jts.shape.CubicBezierCurve;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeometryUtils {
    private static final GeometryFactory geometryFactory = new GeometryFactory();
    private static final Random random = new Random();
    /**
     * Finds a point on the circumference of a circle given an angle in radians.
     * @param centerX X-coordinate of the circle's center.
     * @param centerY Y-coordinate of the circle's center.
     * @param radius Radius of the circle.
     * @param angle Angle in radians where the point lies on the circle.
     * @return A double array containing the X and Y coordinates of the point.
     */
    public static double[] findPointOnCircle(double centerX, double centerY, double radius, double angle) {
        double x = centerX + radius * Math.cos(angle);
        double y = centerY + radius * Math.sin(angle);
        return new double[]{x, y};
    }

    /**
     * Finds the tangents from an external point to a circle.
     * @param centerX X-coordinate of the circle's center.
     * @param centerY Y-coordinate of the circle's center.
     * @param radius Radius of the circle.
     * @param pointX X-coordinate of the external point.
     * @param pointY Y-coordinate of the external point.
     * @return A 2D array with each row representing the X and Y coordinates of the tangent points.
     *         Returns null if the point is inside the circle.
     */
    public static double[][] findTangentsToCircle(double centerX, double centerY, double radius, double pointX, double pointY) {
        double dx = pointX - centerX;
        double dy = pointY - centerY;
        double distanceSquared = dx * dx + dy * dy;
        double radiusSquared = radius * radius;

        // If the point is inside the circle, no tangent exists
        if (distanceSquared < radiusSquared) {
            return null;
        }

        double distance = Math.sqrt(distanceSquared);
        double angleToPoint = Math.atan2(dy, dx);
        double angleOffset = Math.acos(radius / distance);

        // Tangent points on the circle
        double[] tangent1 = findPointOnCircle(centerX, centerY, radius, angleToPoint + angleOffset);
        double[] tangent2 = findPointOnCircle(centerX, centerY, radius, angleToPoint - angleOffset);

        return new double[][]{tangent1, tangent2};
    }
    /**
     * Generates n equally spaced points on the circumference of a circle.
     * @param centerX X-coordinate of the circle's center.
     * @param centerY Y-coordinate of the circle's center.
     * @param radius Radius of the circle.
     * @param n Number of points to generate.
     * @return A 2D array where each row contains the X and Y coordinates of a point on the circle.
     */
    public static double[][] generateEquallySpacedPointsOnCircle(double centerX, double centerY, double radius, int n) {
        double[][] points = new double[n][2];
        double angleIncrement = 2 * Math.PI / n;

        for (int i = 0; i < n; i++) {
            double angle = i * angleIncrement;
            points[i][0] = centerX + radius * Math.cos(angle);
            points[i][1] = centerY + radius * Math.sin(angle);
        }

        return points;
    }
    public static List<Point[]> generatePolygonSides(float[][] vertices) {
        List<Point[]> sides = new ArrayList<>();
        int numVertices = vertices.length;

        for (int i = 0; i < numVertices; i++) {
            // Current point
            Point p1 = new Point(vertices[i][0], vertices[i][1]);

            // Next point, wrapping back to the first vertex at the end
            Point p2 = new Point(vertices[(i + 1) % numVertices][0], vertices[(i + 1) % numVertices][1]);

            // Add the pair of points as a side
            sides.add(new Point[]{p1, p2});
        }

        return sides;
    }
    /**
     * Generates n points on a helical curve starting from a given point (x, y, z).
     * @param startX X-coordinate of the starting point.
     * @param startY Y-coordinate of the starting point.
     * @param startZ Z-coordinate of the starting point.
     * @param radius Radius of the helical curve.
     * @param height Total vertical height the helix will span.
     * @param turns Number of full rotations the helix will make over the given height.
     * @param n Number of points to generate.
     * @return A 2D array with the X, Y, and Z coordinates of each point on the helical curve.
     */
    public static double[][] generateHelicalCurvePoints(double startX, double startY, double startZ,
                                                        double radius, double height, double turns, int n) {
        double[][] points = new double[n][3];
        double angleIncrement = 2 * Math.PI * turns / n; // Angle increment per point
        double heightIncrement = height / n;             // Height increment per point

        for (int i = 0; i < n; i++) {
            double angle = i * angleIncrement;
            points[i][0] = startX + radius * Math.cos(angle);  // X-coordinate
            points[i][1] = startY + radius * Math.sin(angle);  // Y-coordinate
            points[i][2] = startZ + i * heightIncrement;       // Z-coordinate
        }

        return points;
    }
    /**
     * Performs linear interpolation between min and max based on a given factor.
     * @param min The starting value of the range.
     * @param max The ending value of the range.
     * @param factor The interpolation factor (typically between 0 and 1).
     * @return The interpolated value between min and max.
     */
    public static double lerp(double min, double max, double factor) {
        return min + factor * (max - min);
    }
    /**
     * Generates n evenly spaced points on a line between two 3D points.
     * @param startX X-coordinate of the starting point.
     * @param startY Y-coordinate of the starting point.
     * @param startZ Z-coordinate of the starting point.
     * @param endX X-coordinate of the ending point.
     * @param endY Y-coordinate of the ending point.
     * @param endZ Z-coordinate of the ending point.
     * @param n Number of points to generate, including the start and end points.
     * @return A 2D array where each row contains the X, Y, and Z coordinates of a point on the line.
     */
    public static double[][] getPointsOnLine(double startX, double startY, double startZ,
                                                  double endX, double endY, double endZ, int n) {
        double[][] points = new double[n][3];

        // Calculate increments for each coordinate
        double xIncrement = (endX - startX) / (n - 1);
        double yIncrement = (endY - startY) / (n - 1);
        double zIncrement = (endZ - startZ) / (n - 1);

        // Generate points on the line
        for (int i = 0; i < n; i++) {
            points[i][0] = startX + i * xIncrement;
            points[i][1] = startY + i * yIncrement;
            points[i][2] = startZ + i * zIncrement;
        }

        return points;
    }
    public static double[][] generateRandomPointsInBoundingBox(double minX, double maxX,
                                                               double minY, double maxY,
                                                               double minZ, double maxZ, int n) {
        double[][] points = new double[n][3];
        Random rand = new Random();

        for (int i = 0; i < n; i++) {
            double x = minX + (maxX - minX) * rand.nextDouble();
            double y = minY + (maxY - minY) * rand.nextDouble();
            double z = minZ + (maxZ - minZ) * rand.nextDouble();

            points[i][0] = x;
            points[i][1] = y;
            points[i][2] = z;
        }

        return points;
    }
    /**
     * Generates points for a staircase from a start point to an end point in 3D space.
     * @param startX X-coordinate of the starting point.
     * @param startY Y-coordinate of the starting point.
     * @param startZ Z-coordinate of the starting point.
     * @param endX X-coordinate of the ending point.
     * @param endY Y-coordinate of the ending point.
     * @param endZ Z-coordinate of the ending point.
     * @param stairWidth Desired width increment in the X direction per step.
     * @param stairHeight Desired height increment in the Y direction per step.
     * @param delta Desired increment in the Z direction per step.
     * @return A 2D array where each row contains the X, Y, and Z coordinates of a point on the staircase path.
     */
    public static double[][] staircase(double startX, double startY, double startZ,
                                       double endX, double endY, double endZ,
                                       double stairWidth, double stairHeight, double delta) {
        // Calculate total distance between start and end points
        double totalDistanceX = endX - startX;
        double totalDistanceY = endY - startY;
        double totalDistanceZ = endZ - startZ;

        // Calculate the number of steps needed to reach the endpoint exactly
        int stepsX = (int) Math.round(totalDistanceX / stairWidth);
        int stepsY = (int) Math.round(totalDistanceY / stairHeight);
        int stepsZ = (int) Math.round(totalDistanceZ / delta);

        // Total steps required will be the maximum of the steps in each direction
        int steps = Math.max(Math.max(stepsX, stepsY), stepsZ);

        // Adjust stairWidth, stairHeight, and delta to ensure integer steps
        double adjustedWidth = totalDistanceX / steps;
        double adjustedHeight = totalDistanceY / steps;
        double adjustedDelta = totalDistanceZ / steps;

        // Create array to hold points, with an additional point for the end
        double[][] points = new double[steps + 1][3];

        for (int i = 0; i <= steps; i++) {
            points[i][0] = startX + i * adjustedWidth;
            points[i][1] = startY + i * adjustedHeight;
            points[i][2] = startZ + i * adjustedDelta;
        }

        return points;
    }


    /**
     * Checks if a 3D point lies within a polygon defined by vertices on a plane.
     * @param pointX X-coordinate of the point to check.
     * @param pointY Y-coordinate of the point to check.
     * @param pointZ Z-coordinate of the point to check.
     * @param vertices A 2D array of vertices, where each sub-array is an (X, Y, Z) coordinate.
     * @return true if the point lies within the polygon, false otherwise.
     */
    public static boolean isPointInPolygon(double pointX, double pointY, double pointZ, double[][] vertices) {
        int count = vertices.length;
        boolean inside = false;

        for (int i = 0, j = count - 1; i < count; j = i++) {
            double xi = vertices[i][0], yi = vertices[i][1], zi = vertices[i][2];
            double xj = vertices[j][0], yj = vertices[j][1], zj = vertices[j][2];

            if (((yi > pointY) != (yj > pointY)) &&
                    (pointX < (xj - xi) * (pointY - yi) / (yj - yi) + xi)) {
                inside = !inside;
            }
        }

        return inside;
    }


    public static double[][] translate(double[][] points, double dx, double dy, double dz) {
        double[][] translatedPoints = new double[points.length][3];

        for (int i = 0; i < points.length; i++) {
            translatedPoints[i][0] = points[i][0] + dx;
            translatedPoints[i][1] = points[i][1] + dy;
            translatedPoints[i][2] = points[i][2] + dz;
        }

        return translatedPoints;
    }
    public static double[][] rotate(double[][] points, double rx, double ry, double rz) {
        // Convert non-zero angles from degrees to radians
        double radX = rx != 0 ? Math.toRadians(rx) : 0;
        double radY = ry != 0 ? Math.toRadians(ry) : 0;
        double radZ = rz != 0 ? Math.toRadians(rz) : 0;

        // Precompute the sin and cos values for each rotation axis
        double cosX = Math.cos(radX), sinX = Math.sin(radX);
        double cosY = Math.cos(radY), sinY = Math.sin(radY);
        double cosZ = Math.cos(radZ), sinZ = Math.sin(radZ);

        double[][] rotatedPoints = new double[points.length][3];

        for (int i = 0; i < points.length; i++) {
            double x = points[i][0];
            double y = points[i][1];
            double z = points[i][2];

            // Rotation around X-axis (only if rx is not zero)
            double x1 = x;
            double y1 = rx != 0 ? y * cosX - z * sinX : y;
            double z1 = rx != 0 ? y * sinX + z * cosX : z;

            // Rotation around Y-axis (only if ry is not zero)
            double x2 = ry != 0 ? x1 * cosY + z1 * sinY : x1;
            double y2 = y1;
            double z2 = ry != 0 ? -x1 * sinY + z1 * cosY : z1;

            // Rotation around Z-axis (only if rz is not zero)
            rotatedPoints[i][0] = rz != 0 ? x2 * cosZ - y2 * sinZ : x2;
            rotatedPoints[i][1] = rz != 0 ? x2 * sinZ + y2 * cosZ : y2;
            rotatedPoints[i][2] = z2;
        }

        return rotatedPoints;
    }
    /**
     * Generates a closed path that starts and ends at the origin (0, 0, 0),
     * traversing through a set of points in the XZ plane.
     * @param points A 2D array where each row contains the X and Z coordinates of a point on the XZ plane.
     *               The Y-coordinate is assumed to be zero.
     * @return A 2D array where each row contains the X, Y, and Z coordinates of a point on the path,
     *         starting and ending at the origin.
     *         If you have an array of points like {{3, 4}, {5, -2}, {7, 1}}, this function will generate a path starting at (0, 0, 0),
     *         going through (3, 0, 4), (5, 0, -2), (7, 0, 1), and then returning back to (0, 0, 0).
     */
    public static double[][] generateClosedPath(double[][] points, int zeroAxis) {
        int n = points.length;
        double[][] path = new double[n + 2][3];  // +2 for start and end points at (0, 0, 0)

        // Start point at the origin
        path[0][0] = 0.0;
        path[0][1] = 0.0;
        path[0][2] = 0.0;

        // Traverse through the provided points
        for (int i = 0; i < n; i++) {
            if (zeroAxis == 0) {           // YZ plane
                path[i + 1][0] = 0.0;      // X-coordinate fixed to zero
                path[i + 1][1] = points[i][0];  // Y-coordinate
                path[i + 1][2] = points[i][1];  // Z-coordinate
            } else if (zeroAxis == 1) {    // XZ plane
                path[i + 1][0] = points[i][0];  // X-coordinate
                path[i + 1][1] = 0.0;      // Y-coordinate fixed to zero
                path[i + 1][2] = points[i][1];  // Z-coordinate
            } else if (zeroAxis == 2) {    // XY plane
                path[i + 1][0] = points[i][0];  // X-coordinate
                path[i + 1][1] = points[i][1];  // Y-coordinate
                path[i + 1][2] = 0.0;      // Z-coordinate fixed to zero
            }
        }

        // End point returning back to the origin
        path[n + 1][0] = 0.0;
        path[n + 1][1] = 0.0;
        path[n + 1][2] = 0.0;

        return path;
    }
    public static double[][] generateNormalPolygon(double pointX, double pointY, double pointZ, int n, double circumradius) {
        double[][] vertices = new double[n][3];

        // Calculate the direction vector of the line from the origin to the point
        double directionLength = Math.sqrt(pointX * pointX + pointY * pointY + pointZ * pointZ);
        double dirX = pointX / directionLength;
        double dirY = pointY / directionLength;
        double dirZ = pointZ / directionLength;

        // Find two perpendicular vectors on the plane normal to the direction vector
        // We can arbitrarily pick a vector to cross with
        double perp1X = -dirY, perp1Y = dirX, perp1Z = 0;

        if (perp1X == 0 && perp1Y == 0) {  // Special case where dir is along Z-axis
            perp1X = 1.0;  // Use a non-zero component for perp1
        }

        // Normalize the first perpendicular vector
        double perp1Length = Math.sqrt(perp1X * perp1X + perp1Y * perp1Y + perp1Z * perp1Z);
        perp1X /= perp1Length;
        perp1Y /= perp1Length;
        perp1Z /= perp1Length;

        // Calculate the second perpendicular vector by crossing direction and perp1
        double perp2X = dirY * perp1Z - dirZ * perp1Y;
        double perp2Y = dirZ * perp1X - dirX * perp1Z;
        double perp2Z = dirX * perp1Y - dirY * perp1X;

        // Normalize the second perpendicular vector
        double perp2Length = Math.sqrt(perp2X * perp2X + perp2Y * perp2Y + perp2Z * perp2Z);
        perp2X /= perp2Length;
        perp2Y /= perp2Length;
        perp2Z /= perp2Length;

        // Angle between each vertex in radians for the n-sided polygon
        double angleIncrement = 2 * Math.PI / n;

        // Calculate the vertices of the polygon on the plane
        for (int i = 0; i < n; i++) {
            double angle = i * angleIncrement;
            vertices[i][0] = pointX + (perp1X * Math.cos(angle) + perp2X * Math.sin(angle)) * circumradius;
            vertices[i][1] = pointY + (perp1Y * Math.cos(angle) + perp2Y * Math.sin(angle)) * circumradius;
            vertices[i][2] = pointZ + (perp1Z * Math.cos(angle) + perp2Z * Math.sin(angle)) * circumradius;
        }

        return vertices;
    }

    /**
     * Generates n points on a circle at a specified distance from the origin on a specified axis.
     * @param n Number of points to generate.
     * @param axis Axis around which the circle is oriented: 0 for X, 1 for Y, 2 for Z.
     * @param length Radius of the circle.
     * @return A 2D array where each row contains the X, Y, and Z coordinates of a point on the circle.
     */
    public static double[][] generatePointsOnCircle(int n, int axis, double length) {
        double[][] points = new double[n][3];
        double angleIncrement = 2 * Math.PI / n;

        for (int i = 0; i < n; i++) {
            double angle = i * angleIncrement;
            double x = 0, y = 0, z = 0;

            // Determine the plane in which the circle lies based on the axis
            if (axis == 0) {        // Circle in the YZ plane (X-axis as normal)
                x = 0;
                y = length * Math.cos(angle);
                z = length * Math.sin(angle);
            } else if (axis == 1) { // Circle in the XZ plane (Y-axis as normal)
                x = length * Math.cos(angle);
                y = 0;
                z = length * Math.sin(angle);
            } else if (axis == 2) { // Circle in the XY plane (Z-axis as normal)
                x = length * Math.cos(angle);
                y = length * Math.sin(angle);
                z = 0;
            }

            points[i][0] = x;
            points[i][1] = y;
            points[i][2] = z;
        }

        return points;
    }
    /**
     * Generates points on a plane created by lofting two lines in 3D space.
     * @param startLine1 The starting point of the first line (as {x, y, z}).
     * @param endLine1 The ending point of the first line (as {x, y, z}).
     * @param startLine2 The starting point of the second line (as {x, y, z}).
     * @param endLine2 The ending point of the second line (as {x, y, z}).
     * @param n Number of points along each line.
     * @param m Number of divisions between the two lines.
     * @return A 3D array where each row contains the X, Y, and Z coordinates of a point on the lofted plane.
     */
    public static double[][][] loftLines(double[] startLine1, double[] endLine1,
                                         double[] startLine2, double[] endLine2, int n, int m) {
        double[][][] loftedPoints = new double[n][m][3];

        // Calculate the increments along the lines
        double stepX1 = (endLine1[0] - startLine1[0]) / (n - 1);
        double stepY1 = (endLine1[1] - startLine1[1]) / (n - 1);
        double stepZ1 = (endLine1[2] - startLine1[2]) / (n - 1);

        double stepX2 = (endLine2[0] - startLine2[0]) / (n - 1);
        double stepY2 = (endLine2[1] - startLine2[1]) / (n - 1);
        double stepZ2 = (endLine2[2] - startLine2[2]) / (n - 1);

        // Generate points on the lofted plane
        for (int i = 0; i < n; i++) {
            // Points on each line at the ith division
            double x1 = startLine1[0] + i * stepX1;
            double y1 = startLine1[1] + i * stepY1;
            double z1 = startLine1[2] + i * stepZ1;

            double x2 = startLine2[0] + i * stepX2;
            double y2 = startLine2[1] + i * stepY2;
            double z2 = startLine2[2] + i * stepZ2;

            // Interpolate points between corresponding points on the two lines
            for (int j = 0; j < m; j++) {
                double t = (double) j / (m - 1);  // Interpolation factor between 0 and 1

                loftedPoints[i][j][0] = x1 + t * (x2 - x1);  // X-coordinate
                loftedPoints[i][j][1] = y1 + t * (y2 - y1);  // Y-coordinate
                loftedPoints[i][j][2] = z1 + t * (z2 - z1);  // Z-coordinate
            }
        }

        return loftedPoints;
    }
    /**
     * Mirrors a list of 3D points across a specified axis.
     * @param points A 2D array where each row contains the X, Y, and Z coordinates of a point.
     * @param axis The axis to mirror across: 0 for X, 1 for Y, 2 for Z.
     * @return A new 2D array where each row contains the X, Y, and Z coordinates of a mirrored point.
     */
    public static double[][] mirror(double[][] points, int axis) {
        double[][] mirroredPoints = new double[points.length][3];

        for (int i = 0; i < points.length; i++) {
            // Copy original coordinates
            double x = points[i][0];
            double y = points[i][1];
            double z = points[i][2];

            // Mirror the point by negating the specified axis coordinate
            if (axis == 0) {           // Mirror across the X-axis
                mirroredPoints[i][0] = -x;
                mirroredPoints[i][1] = y;
                mirroredPoints[i][2] = z;
            } else if (axis == 1) {    // Mirror across the Y-axis
                mirroredPoints[i][0] = x;
                mirroredPoints[i][1] = -y;
                mirroredPoints[i][2] = z;
            } else if (axis == 2) {    // Mirror across the Z-axis
                mirroredPoints[i][0] = x;
                mirroredPoints[i][1] = y;
                mirroredPoints[i][2] = -z;
            } else {
                throw new IllegalArgumentException("Invalid axis value. Use 0 for X, 1 for Y, or 2 for Z.");
            }
        }

        return mirroredPoints;
    }
    /**
     * Generates k points along a helical path around a given circle center with specified radius and height.
     * @param centerX X-coordinate of the center of the circle.
     * @param centerY Y-coordinate of the center of the circle.
     * @param centerZ Z-coordinate of the center of the circle.
     * @param radius Radius of the circular path for the helix.
     * @param height Total vertical height the helix will span.
     * @param k Number of points to generate along the helical path.
     * @return A 2D array where each row contains the X, Y, and Z coordinates of a point on the helix.
     */
    public static double[][] helix(double centerX, double centerY, double centerZ,
                                   double radius, double height, int k) {
        double[][] helixPoints = new double[k][3];

        // Angle increment for each point to complete multiple turns
        double angleIncrement = 2 * Math.PI / k * (height / radius);  // Adjust for height coverage
        double heightIncrement = height / k;

        for (int i = 0; i < k; i++) {
            double angle = i * angleIncrement;
            helixPoints[i][0] = centerX + radius * Math.cos(angle); // X-coordinate (circle with radius)
            helixPoints[i][1] = centerY + i * heightIncrement;      // Y-coordinate (increasing height)
            helixPoints[i][2] = centerZ + radius * Math.sin(angle); // Z-coordinate (circle with radius)
        }

        return helixPoints;
    }
    /**
     * Interpolates points along a fitted spline given by a set of control points.
     * @param splinePoints A 2D array where each row contains the X, Y, and Z coordinates of a control point on the spline.
     * @param n The total number of points to interpolate along the spline, including the endpoints.
     * @return A 2D array where each row contains the X, Y, and Z coordinates of an interpolated point on the spline.
     */
    public static double[][] interpolateSpline(double[][] splinePoints, int n) {
        // Check for at least two points in the input
        if (splinePoints.length < 2) {
            throw new IllegalArgumentException("Spline must have at least two points for interpolation.");
        }

        double[][] interpolatedPoints = new double[n][3];
        int numSegments = splinePoints.length - 1;

        // Total length along the spline to place each interpolated point
        double totalLength = 0.0;
        double[] segmentLengths = new double[numSegments];

        // Calculate the lengths of each segment
        for (int i = 0; i < numSegments; i++) {
            double dx = splinePoints[i + 1][0] - splinePoints[i][0];
            double dy = splinePoints[i + 1][1] - splinePoints[i][1];
            double dz = splinePoints[i + 1][2] - splinePoints[i][2];
            segmentLengths[i] = Math.sqrt(dx * dx + dy * dy + dz * dz);
            totalLength += segmentLengths[i];
        }

        // Interpolate along the spline based on the total length
        for (int j = 0; j < n; j++) {
            // Proportion along the total length for the current point
            double targetLength = (j / (double)(n - 1)) * totalLength;
            double accumulatedLength = 0.0;

            // Find the corresponding segment for the current interpolation point
            int segmentIndex = 0;
            while (segmentIndex < numSegments - 1 && accumulatedLength + segmentLengths[segmentIndex] < targetLength) {
                accumulatedLength += segmentLengths[segmentIndex];
                segmentIndex++;
            }

            // Calculate interpolation factor within the segment
            double segmentFraction = (targetLength - accumulatedLength) / segmentLengths[segmentIndex];
            double[] start = splinePoints[segmentIndex];
            double[] end = splinePoints[segmentIndex + 1];

            // Interpolate between the start and end points of the segment
            interpolatedPoints[j][0] = start[0] + segmentFraction * (end[0] - start[0]);
            interpolatedPoints[j][1] = start[1] + segmentFraction * (end[1] - start[1]);
            interpolatedPoints[j][2] = start[2] + segmentFraction * (end[2] - start[2]);
        }

        return interpolatedPoints;
    }
    /**
     * Interpolates n points along a line defined by a set of control points.
     * @param linePoints A 2D array where each row contains the X, Y, and Z coordinates of a control point on the line.
     * @param n The total number of points to interpolate along the line, including the endpoints.
     * @return A 2D array where each row contains the X, Y, and Z coordinates of an interpolated point on the line.
     */
    public static double[][] interpolateLine(double[][] linePoints, int n) {
        if (linePoints.length < 2) {
            throw new IllegalArgumentException("Line must have at least two points for interpolation.");
        }

        double[][] interpolatedPoints = new double[n][3];
        int numSegments = linePoints.length - 1;

        // Calculate the total length of the line by summing the lengths of each segment
        double totalLength = 0.0;
        double[] segmentLengths = new double[numSegments];
        for (int i = 0; i < numSegments; i++) {
            double dx = linePoints[i + 1][0] - linePoints[i][0];
            double dy = linePoints[i + 1][1] - linePoints[i][1];
            double dz = linePoints[i + 1][2] - linePoints[i][2];
            segmentLengths[i] = Math.sqrt(dx * dx + dy * dy + dz * dz);
            totalLength += segmentLengths[i];
        }

        // Interpolate points along the line based on the total length
        for (int j = 0; j < n; j++) {
            double targetLength = (j / (double)(n - 1)) * totalLength;
            double accumulatedLength = 0.0;
            int segmentIndex = 0;

            // Find the segment where the current target point lies
            while (segmentIndex < numSegments - 1 && accumulatedLength + segmentLengths[segmentIndex] < targetLength) {
                accumulatedLength += segmentLengths[segmentIndex];
                segmentIndex++;
            }

            // Calculate the interpolation factor within the segment
            double segmentFraction = (targetLength - accumulatedLength) / segmentLengths[segmentIndex];
            double[] start = linePoints[segmentIndex];
            double[] end = linePoints[segmentIndex + 1];

            // Interpolate between the start and end points of the segment
            interpolatedPoints[j][0] = start[0] + segmentFraction * (end[0] - start[0]);
            interpolatedPoints[j][1] = start[1] + segmentFraction * (end[1] - start[1]);
            interpolatedPoints[j][2] = start[2] + segmentFraction * (end[2] - start[2]);
        }

        return interpolatedPoints;
    }
    public static float[][] convertPointsToArray(List<Point> points) {
        float[][] pointArray = new float[points.size()][3];  // Array to hold x, y, z for each point

        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            pointArray[i][0] = p.getX();
            pointArray[i][1] = p.getY();
            pointArray[i][2] = p.getZ();
        }

        return pointArray;
    }

    public static Point randomize(Point point, float xRand, float yRand, float zRand) {
        float newX = point.getX() + (random.nextFloat() * 2 - 1) * xRand;
        float newY = point.getY() + (random.nextFloat() * 2 - 1) * yRand;
        float newZ = point.getZ() + (random.nextFloat() * 2 - 1) * zRand;
        return new Point(newX, newY, newZ);
    }
    public static List<Point> randomizePoints(List<Point> points, float xRand, float yRand, float zRand) {
        List<Point> randomizedPoints = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            randomizedPoints.add(randomize(points.get(i), xRand, yRand, zRand));
        }
        return randomizedPoints;
    }
    public static List<Point> generatePointsOnPath(List<Point> path, int n, boolean closePath) {
        List<Coordinate> coordinates = new ArrayList<>();

        // Convert Points to Coordinates
        for (Point point : path) {
            coordinates.add(new Coordinate(point.getX(), point.getY(), point.getZ()));
        }

        // Close the path if required
        if (closePath && !coordinates.isEmpty()) {
            coordinates.add(coordinates.get(0));
        }

        // Create LineString from coordinates
        LineString lineString = geometryFactory.createLineString(coordinates.toArray(new Coordinate[0]));

        // Use LengthIndexedLine to calculate points along the path
        LengthIndexedLine indexedLine = new LengthIndexedLine(lineString);
        double totalLength = indexedLine.getEndIndex();
        double interval = totalLength / (n - 1);

        // Generate points at equal intervals along the path
        List<Point> generatedPoints = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Coordinate coord = indexedLine.extractPoint(i * interval);
            generatedPoints.add(new Point((float)   coord.getX(), (float)coord.getY(),(float) coord.getZ()));

        }

        return generatedPoints;
    }
    public static List<Point> generateOffsetPolygon(List<Point> points, boolean closeOrNot, float offset) {
        List<Coordinate> coordinates = new ArrayList<>();

        // Convert Points to Coordinates
        for (Point point : points) {
            coordinates.add(new Coordinate(point.getX(), point.getY(), point.getZ()));
        }

        // Close the polygon if required
        if (closeOrNot && !coordinates.isEmpty() && !coordinates.get(0).equals(coordinates.get(coordinates.size() - 1))) {
            coordinates.add(coordinates.get(0));
        }

        // Create LineString from coordinates
        LineString lineString = geometryFactory.createLineString(coordinates.toArray(new Coordinate[0]));

        // Convert LineString to Polygon for buffering
        Polygon polygon = geometryFactory.createPolygon(lineString.getCoordinates());

        // Create buffer parameters for offset (positive for outward, negative for inward)
        BufferParameters bufferParams = new BufferParameters();
        Geometry offsetPolygon = BufferOp.bufferOp(polygon, offset);

        // Extract offset polygon points
        int i = 0;
        List<Point> offsetPolygonPoints = new ArrayList<>();
        for (Coordinate coord : offsetPolygon.getCoordinates()) {
            offsetPolygonPoints.add(new Point((float)   coord.getX(), (float)coord.getY(),0));
            i++;
        }

        return offsetPolygonPoints;
    }

    public static List<Point> getConvexHull(List<Point> points) {
        // Convert List<Point> to an array of Coordinates
        Coordinate[] coordinates = new Coordinate[points.size()];
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            coordinates[i] = new Coordinate(point.getX(), point.getY(), point.getZ());
        }

        // Create a ConvexHull object and compute the hull
        ConvexHull convexHull = new ConvexHull(coordinates, geometryFactory);
        Geometry hullGeometry = convexHull.getConvexHull();

        // Extract the points from the resulting hull geometry
        List<Point> hullPoints = new ArrayList<>();
        for (Coordinate coord : hullGeometry.getCoordinates()) {
            hullPoints.add(new Point((float)   coord.getX(), (float)coord.getY(),(float) coord.getZ()));
        }


        return hullPoints;
    }
    public static Circle getMaxInscribedCircle(List<Point> points, double tolerance) {
        // Convert the list of points to a polygon
        Coordinate[] coordinates = new Coordinate[points.size() + 1];
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            coordinates[i] = new Coordinate(point.getX(), point.getY(), point.getZ());
        }
        // Close the polygon by adding the first point at the end
        coordinates[points.size()] = coordinates[0];

        Polygon polygon = geometryFactory.createPolygon(coordinates);

        // Start with the centroid as an initial center point
        org.locationtech.jts.geom.Point center = polygon.getCentroid();
        double radius = getDistanceToBoundary(polygon, center);

        // Iteratively refine the center for maximum inscribed circle
        boolean improved = true;
        while (improved) {
            improved = false;
            Coordinate[] boundaryCoords = polygon.getExteriorRing().getCoordinates();

            // Find the point on the boundary closest to the current center
            Coordinate closestBoundaryPoint = null;
            double minDist = Double.MAX_VALUE;
            for (Coordinate coord : boundaryCoords) {
                double dist = center.getCoordinate().distance(coord);
                if (dist < minDist) {
                    minDist = dist;
                    closestBoundaryPoint = coord;
                }
            }

            // Move the center closer to the farthest point inside the polygon
            Coordinate newCenterCoord = new Coordinate(
                    (center.getX() + closestBoundaryPoint.x) / 2,
                    (center.getY() + closestBoundaryPoint.y) / 2
            );
            org.locationtech.jts.geom.Point newCenter = geometryFactory.createPoint(newCenterCoord);

            // Calculate the new radius
            double newRadius = getDistanceToBoundary(polygon, newCenter);

            // Check if the new center gives a larger inscribed circle
            if (newRadius > radius + tolerance) {
                center = newCenter;
                radius = newRadius;
                improved = true;
            }
        }

        return new Circle(center, radius);
    }

    private static double getDistanceToBoundary(Polygon polygon, org.locationtech.jts.geom.Point point) {
        DistanceOp distOp = new DistanceOp(polygon.getExteriorRing(), point);
        return distOp.distance();
    }

}
