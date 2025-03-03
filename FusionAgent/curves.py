import adsk.core, adsk.fusion, adsk.cam, traceback
from http.server import BaseHTTPRequestHandler, HTTPServer
import threading
import json
import os
def draw_fitted_spline(points):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent

    # Create a new sketch on the XY plane (or any other desired plane)
    sketches = rootComp.sketches
    sketch = sketches.add(rootComp.xYConstructionPlane)

    # Create a collection of points for the spline
    spline_points = adsk.core.ObjectCollection.create()
    for point in points:
        spline_point = adsk.core.Point3D.create(point[0], point[1], point[2])
        spline_points.add(spline_point)

    # Add the fitted spline through the points
    sketch.sketchCurves.sketchFittedSplines.add(spline_points)

def draw_conic_curve(start_point, end_point,apex_point,rho_value):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent

    # Create a new sketch on the XY plane (or any other desired plane)
    sketches = rootComp.sketches
    sketch = sketches.add(rootComp.xYConstructionPlane)

    # Define points for the conic curve
    start = adsk.core.Point3D.create(start_point[0], start_point[1], start_point[2])
    end = adsk.core.Point3D.create(end_point[0], end_point[1], end_point[2])
    apex = adsk.core.Point3D.create(apex_point[0], apex_point[1], apex_point[2])


    # Add the conic curve to the sketch
    sketch.sketchCurves.sketchConicCurves.add(start, end,apex, rho_value)

start_point = [0, 0, 0]
end_point = [10, 0, 0]
apex = [5, 5, 0]
draw_conic_curve(start_point, end_point, apex, .9)