#Author-
#Description-

import adsk.core, adsk.fusion, adsk.cam, traceback

def run(context):
    ui = None
    try:
        app = adsk.core.Application.get()
        ui  = app.userInterface
        ui.messageBox('Hello script')
        design = app.activeProduct

        # Get the root component of the active design.
        rootComp = design.rootComponent

        # Create a new sketch on the xy plane.
        sketch = rootComp.sketches.add(rootComp.xYConstructionPlane)

        # Create an object collection for the points.
        points = []

        # Define the points the spline with fit through.
        points.append(adsk.core.Point3D.create(0, 0, 0))
        points.append(adsk.core.Point3D.create(5*2.54, 1*2.54, 0))
        points.append(adsk.core.Point3D.create(6*2.54, 4*2.54, 3*2.54))
        points.append(adsk.core.Point3D.create(7*2.54, 6*2.54, 6*2.54))
        points.append(adsk.core.Point3D.create(2*2.54, 3*2.54, 0))
        points.append(adsk.core.Point3D.create(0, 1*2.54, 0))
        lines = sketch.sketchCurves.sketchLines
       
        # Create the spline. 
        for i in range(0,len(points)-1):
           startPoint = points[i]
           endPoint = points[i+1]
           line = lines.addByTwoPoints(startPoint, endPoint)
        line = lines.addByTwoPoints(points[len(points)-1], points[0])
        
    except:
        if ui:
            ui.messageBox('Failed:\n{}'.format(traceback.format_exc()))
