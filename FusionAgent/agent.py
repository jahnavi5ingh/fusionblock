import adsk.core, adsk.fusion, adsk.cam, traceback
from http.server import BaseHTTPRequestHandler, HTTPServer
import threading
import json
import os

# Define a global variable to store the server instance
httpd = None
target_plane = None
class RequestHandler(BaseHTTPRequestHandler):
    def do_POST(self):
        global httpd
        content_length = int(self.headers['Content-Length'])
        post_data = self.rfile.read(content_length)
        data = json.loads(post_data)
        try:
            if 'command' in data:
                command = data['command']
                params = data.get('params', {})
                response = self.handle_command(command, params)

                self.send_response(response['status'])
                self.send_header('Content-type', 'application/json')
                self.end_headers()
                self.wfile.write(json.dumps(response).encode())
            else:
                self.send_response(400)
                self.send_header('Content-type', 'application/json')
                self.end_headers()
                response = {'status': 'error', 'message': 'No command provided'}
                self.wfile.write(json.dumps(response).encode())
        except Exception as e:
            self.send_response(500)
            self.send_header('Content-type', 'application/json')
            self.end_headers()
            response = {'status': 'error', 'message': str(e)}
            self.wfile.write(json.dumps(response).encode())

    def handle_command(self, command, params):
        try:
            if command == 'draw_point':
                x, y, z = params['x'], params['y'], params['z']
                draw_point(x, y, z)
                return {'status': 200, 'message': 'Point drawn successfully'}
            elif command == 'draw_points':
                points = params['points']  # Expecting a list of [x, y, z] points
                draw_points(points)
                return {'status': 200, 'message': f"{len(points)} points added to sketch on XY plane."}
            elif command == 'draw_line':
                pt1, pt2, name = params['pt1'], params['pt2'], params.get('name', 'Unnamed Line')
                draw_line(pt1, pt2, name)
                return {'status': 200, 'message': f'Line "{name}" drawn successfully'}
            elif command == 'draw_lines':
                points, name, close = params['points'], params.get('name', 'Unnamed Lines'), params.get('close', False)
                draw_lines(points, name, close)
                return {'status': 200, 'message': f'Lines "{name}" drawn successfully'}
            elif command == 'draw_lines_pair':
                points,points2, name = params['points'],params['points2'], params.get('name', 'Unnamed Lines')
                draw_lines_pair(points,points2, name)
                return {'status': 200, 'message': f'Lines "{name}" drawn successfully'}

            elif command == 'draw_circle':
                pt1, radius, name = params['pt1'], params['radius'], params.get('name', 'Unnamed Circle')
                draw_circle(pt1, radius, name)
                return {'status': 200, 'message': f'Circle "{name}" drawn successfully'}
            elif command == 'draw_rectangle':
                pt1, pt2, name = params['pt1'], params['pt2'], params.get('name', 'Unnamed Rectangle')
                draw_rectangle(pt1, pt2, name)
                return {'status': 200, 'message': f'Rectangle "{name}" drawn successfully'}
            elif command == 'draw_centered_rectangle':
                pt1, width, height, name = params['pt1'], params['width'], params['height'], params.get('name', 'Unnamed Centered Rectangle')
                draw_centered_rectangle(pt1, width, height, name)
                return {'status': 200, 'message': f'Centered Rectangle "{name}" drawn successfully'}
            elif command == 'add_offset_plane':
                base_plane_name = params['base_plane']  # Should be "xy", "yz", or "xz"
                offset = params['offset']
                plane_name = params.get('name', 'OffsetPlane')
                add_offset_plane(base_plane_name, offset, plane_name)
                return {'status': 200, 'message': f'Plane "{plane_name}" added at {offset} units offset from {base_plane_name} plane'}
            elif command == 'extrude':
                bodyName = params['bodyName']
                name, offset,taperAngle = params['name'], params['offset'], params['taperAngle']
                extrude_shape(name, offset, taperAngle,bodyName)
                return {'status': 200, 'message': f'Shape "{name}" extruded by {offset}'}
            elif command == 'revolve':
                bodyName = params['bodyName']
                name, axis_index, angle = params['name'], params['axis_index'], params.get('angle', 360)
                revolve_shape(name, axis_index, angle,bodyName)
                return {'status': 200, 'message': f'Sketch "{name}" revolved successfully'}
            elif command == 'quit':
                threading.Thread(target=self.shutdown_server).start()
                return {'status': 200, 'message': 'Server is shutting down...'}
            elif command == 'export':
                filename = params.get('filename', 'out.obj')
                file_path = os.path.join('C:\\temp', filename)
                export_design(file_path)
                return {'status': 200, 'message': f'Design exported to {file_path}'}
            elif command == 'duplicate':
                body_name = params['body']
                copy_name = params.get('copy_name', f'{body_name}_copy')
                x, y, z = params['x'], params['y'], params['z']
                duplicate_body(body_name, x, y, z,copy_name)
                return {'status': 200, 'message': f'Body "{body_name}" duplicated and moved to ({x}, {y}, {z})'}
            elif command == 'cut':
                body1_name = params['body1']
                body2_name = params['body2']
                cut(body1_name, body2_name)
                return {'status': 200, 'message': f'Resulting body cut'}
            elif command == 'move':
                body_name = params['body']
                x, y, z = params['x'], params['y'], params['z']
                move_body(body_name, x, y, z)
                return {'status': 200, 'message': f'Body "{body_name}" moved to ({x}, {y}, {z})'}
            elif command == 'rotate':
                body_name = params['body']
                axis = params['axis']  # Should be 'x', 'y', or 'z'
                angle = params['angle']
                rotate_body(body_name, axis, angle)
                return {'status': 200, 'message': f'Body "{body_name}" rotated {angle} degrees around {axis}-axis'}
            elif command == 'loft_between_sketches':
                sketch1_name = params['sketch1']
                sketch2_name = params['sketch2']
                loft_name = params.get('name', 'LoftFeature')
                loft_between_sketches(sketch1_name, sketch2_name, loft_name)
                return {'status': 200, 'message': f'Loft "{loft_name}" created between sketches "{sketch1_name}" and "{sketch2_name}"'}

            elif command == 'scale':
                body_name = params['body']
                scale_factor = params['scale_factor']
                scale_body(body_name, scale_factor)
                return {'status': 200, 'message': f'Body "{body_name}" scaled by a factor of {scale_factor}'}

            elif command == 'hide':
                body_name = params['body']
                hide_body(body_name)
                return {'status': 200, 'message': f'Body "{body_name}" is now hidden'}

            elif command == 'show':
                body_name = params['body']
                show_body(body_name)
                return {'status': 200, 'message': f'Body "{body_name}" is now visible'}

            elif command == 'add_plane_through_points':
                points = params['points']  # Should be a list of three points, each containing [x, y, z]
                plane_name = params.get('name', 'Unnamed Plane')
                add_plane_through_points(points, plane_name)
                return {'status': 200, 'message': f'Plane "{plane_name}" added through three points'}
            elif command == 'select_plane':
                plane_name = params['name']
                select_plane(plane_name)
                return {'status': 200, 'message': f'Plane \"{plane_name}\" is now the active plane for sketching'}
            elif command == 'join_bodies':
                body_names = params['bodies']  # List of body names
                result_name = params.get('name', 'JoinedBody')
                join_bodies(body_names, result_name)
                return {'status': 200, 'message': f'Bodies joined into "{result_name}"'}
            elif command == 'draw_shape_on_plane':
                plane_name = params['target_plane']     # Name of the plane to draw on
                points = params['points']               # List of points, e.g., [[x1, y1, z1], [x2, y2, z2], ...]
                connections = params['connections']     # List of connections, e.g., ['line', 'curve', 'g1', 'g2']
                shape_name = params.get('name', 'CustomShape')
                draw_shape_on_plane(plane_name, points, connections, shape_name)
                return {'status': 200, 'message': f'Shape "{shape_name}" drawn on plane "{plane_name}"'}

            elif command == 'loft_through_sketches':
               sketch_names = params['sketches']  # List of sketch names
               loft_name = params.get('name', 'LoftFeature')
               loft_through_sketches(sketch_names, loft_name)
               return {'status': 200, 'message': f'Loft "{loft_name}" created through sketches {sketch_names}'}
            elif command == 'sweep_sketch_along_path':
               profile_sketch_name = params['profile_sketch']  # Name of the sketch to be swept
               path_sketch_name = params['path_sketch']        # Name of the sketch defining the path
               sweep_name = params.get('name', 'SweepFeature')
               rotation= params.get('rotation_angle', 0)
               orienation = params.get('orientation', 1)
               sweep_sketch_along_path(profile_sketch_name, path_sketch_name, sweep_name,rotation,orienation)
               return {'status': 200, 'message': f'Sweep "{sweep_name}" created with profile "{profile_sketch_name}" along path "{path_sketch_name}"'}
            elif command == 'toggle_sketch_visibility':
                sketch_name = params['sketch_name']
                toggle_sketch_visibility(sketch_name)
                return {'status': 200, 'message': f'Sketch "{sketch_name}" visibility toggled'}

            elif command == 'hide_all_sketches':
                hide_all_sketches()
                return {'status': 200, 'message': 'All sketches have been hidden'}
            elif command == 'hide_all_bodies':
                hide_all_bodies()
                return {'status': 200, 'message': 'All bodies have been hidden'}

            elif command == 'rename_body':
                current_name = params['current_name']  # The existing name of the body
                new_name = params['new_name']          # The new name to assign
                rename_body(current_name, new_name)
                return {'status': 200, 'message': f'Body "{current_name}" renamed to "{new_name}"'}

            elif command == 'add_text':
                text_content = params['text']               # Text to display
                plane_name = params['target_plane']         # Name of the plane to draw the text on
                position1 = params['position1']               # Position [x, y, z] to place the text
                position2 = params['position2']
                font_size = params.get('font_size', 1.0)    # Optional font size with a default value of 1.0
                add_text(text_content, plane_name, position1,position2, font_size)
                return {'status': 200, 'message': f'Text "{text_content}" added to plane "{plane_name}" at position {position}'}

            elif command == 'draw_fitted_spline':
                points = params['points']           # List of points for the spline [[x1, y1, z1], [x2, y2, z2], ...]
                name = params.get('name', 'Spline') # Optional name for the sketch
                draw_fitted_spline(points, name)
                return {'status': 200, 'message': f'Fitted spline created with name "{name}" through points {points}'}

            elif command == 'draw_conic_curve':
                start_point = params['start_point']     # Start point of the conic curve [x, y, z]
                end_point = params['end_point']         # End point of the conic curve [x, y, z]
                apex_point = params['apex_point']       # Apex point of the conic curve [x, y, z]
                rho_value = params['rho_value']         # Rho value for the conic curve (0 <= rho <= 1)
                name = params.get('name', 'Conic')      # Optional name for the sketch
                draw_conic_curve(start_point, end_point, apex_point, rho_value, name)
                return {'status': 200, 'message': f'Conic curve created with name "{name}" from {start_point} to {end_point} passing through {apex_point} with rho {rho_value}'}

            elif command == 'create_3_point_arc':
                start_point = params['start_point']
                point_on_arc = params['point_on_arc']
                end_point = params['end_point']
                name = params.get('name', '3PointArcSketch')
                create_3_point_arc(start_point, point_on_arc, end_point, name)
                return {'status': 200, 'message': f'3-point arc created with sketch name "{name}" from {start_point} through {point_on_arc} to {end_point}'}

            elif command == 'create_start_center_sweep_arc':
                center_point = params['center_point']
                start_point = params['start_point']
                sweep_angle = params['sweep_angle']  # Angle should be in radians
                name = params.get('name', 'StartCenterSweepArcSketch')
                create_start_center_sweep_arc(center_point, start_point, sweep_angle, name)
                return {'status': 200, 'message': f'Arc created with sketch name "{name}", center {center_point}, starting at {start_point} with a sweep angle of {sweep_angle} radians'}

#here comes stage 2 surface modelling premitives

            elif command == 'surface_extrude':
                profile_name = params['profile_name']
                distance = params['distance']
                taperAngle = params['taper_angle']
                item_index = params['item_index']
                name = params.get('name', 'some name')
                surface_extrude(profile_name, taperAngle, distance,name,item_index)
                return {'status': 200, 'message': f"Surface extrude created from profile '{profile_name}' with distance {distance}."}

            elif command == 'surface_revolve':
                profile_name = params['profile_name']
                axis_name = params['axis_name']
                angle = params['angle']
                name = params.get('name', 'some name')
                item_index = params['item_index']
                surface_revolve(profile_name, axis_name, angle,name,item_index)
                return {'status': 200, 'message': f"Surface revolve created from profile '{profile_name}' around axis '{axis_name}' with angle {angle}."}

            elif command == 'surface_sweep':
                profile_name = params['profile_name']
                path_name = params['path_name']
                twist_angle = params.get('twist_angle', 0)
                orientation = params.get('orientation', 1)
                name = params.get('name', 'some name')
                surface_sweep(profile_name,twist_angle, path_name,name,orientation)
                return {'status': 200, 'message': f"Surface sweep created from profile '{profile_name}' along path '{path_name}'."}

            elif command == 'surface_loft':
                profile_names = params['profile_names']
                name = params.get('name', 'some name')
                surface_loft(profile_names,name)
                return {'status': 200, 'message': f"Surface loft created between profiles: {', '.join(profile_names)}."}

            elif command == 'thicken_surfaces':
                body_names = params['body_names']
                thickness = params['thickness']
                thicken_surfaces(body_names, thickness)
                return {'status': 200, 'message': f"Surface bodies '{', '.join(body_names)}' thickened by {thickness}."}

            elif command == 'surface_patch':
                profile_name = params['profile_name']
                body_name = params['body_name']
                item_index = params['item_index']
                surface_patch(profile_name,item_index, body_name)
                return {'status': 200, 'message': f"Surface patch created from profile '{profile_name}' and named '{body_name}'."}

            elif command == 'create_pipe':
                path_name = params['path_name']
                section_size = params['section_size']
                section_thickness = params.get('section_thickness', 0)
                section_type = params.get('section_type', 'Circular')
                create_pipe(path_name, section_size, section_thickness, section_type)
                return {'status': 200, 'message': f"Pipe created along path '{path_name}' with {section_type} profile, size {section_size}, and thickness {section_thickness}."}

            elif command == 'create_plane_along_path':
                plane_name = params['name']
                path_name = params['path_name']
                distance = params['distance']
                create_plane_along_path(path_name, distance,plane_name)
                return {'status': 200, 'message': f"Construction plane created along path '{path_name}' at distance {distance}."}

            elif command == 'create_n_planes_along_path':
                plane_name = params['name']
                path_name = params['path_name']
                n = params['n']
                create_n_planes_along_path(path_name, n,plane_name)
                return {'status': 200, 'message': f"{n} planes created along path '{path_name}'."}

            elif command == 'create_point_along_path':
                path_name = params['path_name']
                distance = params['distance']
                plane_name = params['name']
                pt = create_point_along_path(path_name, distance,plane_name)
                return {'status': 200, 'message': f"Point created along path '{path_name}' at distance {distance}.","x":pt.x,"y":pt.y,"z":pt.z}
            elif command == 'create_tangentplane_along_path':
                path_name = params['path_name']
                distance = params['distance']
                plane_name = params['name']
                dirx = params['dirx']
                diry = params['diry']
                n = params["n"]
                points  = create_tangentplane_along_path(path_name, dirx,diry,distance, plane_name,n)
                return {'status': 200, 'message': f"Point created along path '{path_name}' at distance {distance}.","points":points}



            elif command == 'create_n_points_along_path':
                path_name = params['path_name']
                n = params['n']
                plane_name = params['name']
                points  = create_n_points_along_path(path_name, n,plane_name)
                return {'status': 200, 'message': f"{n} points created along path '{path_name}'.","points":points}

            elif command == 'drawCurves':

                paths = params['paths']
                name = params['name']
                count = params['count']
                points  = draw_curves(name, paths,count)
                return {'status': 200, 'message': f"{count} points created along path '{path_name}'.","points":points}

            else:
                return {'status': 400, 'message': 'Unknown command'}
        except Exception as e:
            return {'status': 500, 'message': str(e)}

    def shutdown_server(self):
        global httpd
        httpd.shutdown()
        os._exit(0)

def create_tangentplane_along_path(path_name,dirx,diry, distance,plane_name,n):
   pt0 = create_point_along_path(path_name, distance,plane_name)
   select_plane(plane_name)
   pt1 = draw_point(dirx,diry,0)
   select_plane("xy")
   draw_line([pt0.x, pt0.y,pt0.z]  ,[pt1.x,pt1.y,pt1.z],plane_name+"_tline")
   points = []
   create_n_planes_along_path(plane_name+"_tline", n,plane_name+"_tangent")
   for i in range(n):
       select_plane(plane_name+"_tangent"+"_"+str(i))
       pt = draw_point(0,0,0)
       points.append({"x":pt.x,"y":pt.y,"z":pt.z})

   return points

def create_point_along_path(path_name, distance,plane_name):
   create_plane_along_path(path_name, distance,plane_name)
   select_plane(plane_name)
   pt = draw_point(0,0,0)
   return pt

def create_n_points_along_path(path_name, n,plane_name):
    #create arrray of points to store and return
    points = []
    create_n_planes_along_path(path_name, n,plane_name)
    for i in range(n):
        select_plane(plane_name+"_"+str(i))
        pt = draw_point(0,0,0)
        points.append({"x":pt.x,"y":pt.y,"z":pt.z})
    return points
#pipe

def create_pipe(path_name, section_size, section_thickness=0, section_type='Circular'):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent

    # Find the path sketch by name and collect curves
    path_sketch = None
    for sketch in rootComp.sketches:
        if sketch.name == path_name:
            path_sketch = sketch
            break
    if not path_sketch:
        print(f"Error: Path sketch '{path_name}' not found.")
        return

    path_curves = adsk.core.ObjectCollection.create()
    for curve in path_sketch.sketchCurves:
        path_curves.add(curve)
    path = rootComp.features.createPath(path_curves)

    # Create the pipe input
    pipes = rootComp.features.pipeFeatures
    pipe_input = pipes.createInput(path, adsk.fusion.FeatureOperations.NewBodyFeatureOperation)

    # Set the section size (e.g., diameter for circular, side length for square/triangular)
    section_size_value = adsk.core.ValueInput.createByReal(section_size)
    pipe_input.sectionSize = section_size_value

    # Set the section thickness if a hollow pipe is required
    if section_thickness > 0:
        pipe_input.isHollow = True
        section_thickness_value = adsk.core.ValueInput.createByReal(section_thickness)
        pipe_input.sectionThickness = section_thickness_value
    else:
        pipe_input.isHollow = False

   # Set the section type (Circular, Square, or Triangular)
    if section_type == 'Circular':
        pipe_input.sectionType = adsk.fusion.PipeSectionTypes.CircularPipeSectionType
    elif section_type == 'Square':
        pipe_input.sectionType = adsk.fusion.PipeSectionTypes.SquarePipeSectionType
    elif section_type == 'Triangular':
        pipe_input.sectionType = adsk.fusion.PipeSectionTypes.TriangularPipeSectionType
    else:
        print("Error: Invalid section type. Use 'Circular', 'Square', or 'Triangular'.")
        return

    # Create the pipe feature
    pipes.add(pipe_input)
    print(f"Pipe created along path '{path_name}' with {section_type} profile, size {section_size}, and thickness {section_thickness}.")

#planes along path
def create_n_planes_along_path(path_name, n,plane_name):
    if n < 2:
        print("Error: The number of planes (n) must be 2 or greater.")
        return

    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent

    # Find the path sketch by name and collect curves
    path_sketch = None
    for sketch in rootComp.sketches:
        if sketch.name == path_name:
            path_sketch = sketch
            break
    if not path_sketch:
        print(f"Error: Path sketch '{path_name}' not found.")
        return

    # Collect curves in the path
    path_curves = adsk.core.ObjectCollection.create()
    for curve in path_sketch.sketchCurves:
        path_curves.add(curve)
    path = rootComp.features.createPath(path_curves)

    # Calculate the normalized distance increment
    distance_increment = 1 / (n - 1)

    # Create planes along the path at each normalized interval
    planes = rootComp.constructionPlanes
    for i in range(n):
        normalized_distance = distance_increment * i
        plane_input = planes.createInput()
        plane_input.setByDistanceOnPath(path, adsk.core.ValueInput.createByReal(normalized_distance))
        plane = planes.add(plane_input)
        plane.name = plane_name +"_"+str(i)
        print(f"Construction plane {i+1} created at normalized distance {normalized_distance} along path '{path_name}'.")

    print(f"{n} planes created along path '{path_name}'.")

def create_plane_along_path(path_name, distance,plane_name):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent

    # Find the path sketch by name and collect curves
    path_sketch = None
    for sketch in rootComp.sketches:
        if sketch.name == path_name:
            path_sketch = sketch
            break
    if not path_sketch:
        print(f"Error: Path sketch '{path_name}' not found.")
        return

    # Create an ObjectCollection for the path curves
    path_curves = adsk.core.ObjectCollection.create()
    for curve in path_sketch.sketchCurves:
        path_curves.add(curve)
    path = rootComp.features.createPath(path_curves)

    # Create the plane along path input
    planes = rootComp.constructionPlanes
    plane_input = planes.createInput()
    plane_input.setByDistanceOnPath(path, adsk.core.ValueInput.createByReal(distance))

    # Add the construction plane
    plane = planes.add(plane_input)
    plane.name = plane_name
    print(f"Construction plane created along path '{path_name}' at distance {distance}.")

#surface  modelling
def surface_patch(profile_name,item_index, body_name):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent

    # Find the profile by name
    curveToDraw = None
    sketchProfile = None
    for sketch in rootComp.sketches:
        if sketch.name == profile_name:
          sketchProfile = sketch
          curveToDraw = sketch.sketchCurves[item_index]

    comp = sketchProfile.parentComponent
    openProfile = comp.createOpenProfile(curveToDraw, False)

    if not openProfile:
        print(f"Error: Profile in sketch '{profile_name}' not found.")
        return

    # Create the surface patch input
    patches = rootComp.features.patchFeatures
    patch_input = patches.createInput(openProfile, adsk.fusion.FeatureOperations.NewBodyFeatureOperation)

    # Create the surface patch
    patch = patches.add(patch_input)

    # Set the body name if the patch was created successfully
    if patch.bodies.count > 0:
        surface_body = patch.bodies.item(0)
        surface_body.name = body_name
        print(f"Surface patch created from profile '{profile_name}' and named '{body_name}'.")
    else:
        print("Error: Surface patch was created but no body found to rename.")

def surface_extrude(profile_name, taper_angle, distance,name,item_index):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent

    sketchProfile = None
    pros = []
    for sketch in rootComp.sketches:
        if sketch.name == profile_name:
          sketchProfile = sketch
          comp = sketchProfile.parentComponent
          for curve in sketchProfile.sketchCurves:
                pros.append(comp.createOpenProfile(curve, False))


    if not pros:
        print(f"Error: Profile in sketch '{profile_name}' not found.")
        return


    comp = sketchProfile.parentComponent

    for openProfile in pros:
        # Create the extrude input for a surface
        extrudes = rootComp.features.extrudeFeatures
        extrude_input = extrudes.createInput(openProfile    , adsk.fusion.FeatureOperations.NewBodyFeatureOperation)

        # Set the distance for the extrusion
        distance_value = adsk.core.ValueInput.createByReal(distance)
        extrude_input.setDistanceExtent(False, distance_value)
        extrude_input.isSolid = False
        # Create the surface extrude
        taper_angle_rad = taper_angle * (math.pi / 180)
        angle = adsk.core.ValueInput.createByReal(taper_angle_rad)
        extrude_input.taperAngle = angle
        extrudedObj = extrudes.add(extrude_input)
        extrudedObj.bodies[0].name = name
    print(f"Surface extrude created from profile '{profile_name}' with distance {distance}.")


def surface_revolve(profile_name, axis_name, angle,name,item_index):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent

     # Find the profile by name

    sketchProfile = None
    pros = []
    for sketch in rootComp.sketches:
        if sketch.name == profile_name:
          sketchProfile = sketch
          comp = sketchProfile.parentComponent
          for curve in sketchProfile.sketchCurves:
                pros.append(comp.createOpenProfile(curve, False))


    if not pros:
        print(f"Error: Profile in sketch '{profile_name}' not found.")
        return



    # Find the axis by name
    axis = None

    if axis_name == "X":
        axis = rootComp.xConstructionAxis
    elif axis_name == "Y":
        axis = rootComp.yConstructionAxis
    elif axis_name == "Z":
        axis = rootComp.zConstructionAxis
    else:
        show_message(f'Invalid axis index {axis_index}. Use 0 for X, 1 for Y, or 2 for Z.')
        return

    if not axis:
        print(f"Error: Axis '{axis_name}' not found.")
        return

    for openProfile in pros:
        # Create the revolve input for a surface
        revolves = rootComp.features.revolveFeatures
        revolve_input = revolves.createInput(openProfile, axis, adsk.fusion.FeatureOperations.NewBodyFeatureOperation)

        # Set the revolve angle

        angle_rad = angle * (math.pi / 180)
        angle_value = adsk.core.ValueInput.createByReal(angle_rad)
        revolve_input.setAngleExtent(False, angle_value)
        revolve_input.isSolid = False
        # Create the surface revolve
        revolveObj = revolves.add(revolve_input)
        revolveObj.bodies[0].name = name


    print(f"Surface revolve created from profile '{profile_name}' around axis '{axis_name}' with angle {angle}.")

def surface_sweep(profile_name,twist_angle, path_name,name,orientation=1):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent

    sketchProfile = None
    pros = []
    for sketch in rootComp.sketches:
        if sketch.name == profile_name:
          sketchProfile = sketch
          comp = sketchProfile.parentComponent
          for curve in sketchProfile.sketchCurves:
                pros.append(comp.createOpenProfile(curve, False))

    comp = sketchProfile.parentComponent



    # Find the path sketch by name and collect curves
    path_sketch = None
    for sketch in rootComp.sketches:
        if sketch.name == path_name:
            path_sketch = sketch
            break
    if not path_sketch:
        print(f"Error: Path sketch '{path_name}' not found.")
        return

    path_curves = adsk.core.ObjectCollection.create()
    for curve in path_sketch.sketchCurves:
        path_curves.add(curve)
    path = rootComp.features.createPath(path_curves)

    for openProfile in pros:
        # Create the sweep input for a surface
        sweeps = rootComp.features.sweepFeatures
        sweep_input = sweeps.createInput(openProfile, path, adsk.fusion.FeatureOperations.NewBodyFeatureOperation)
        sweep_input.isSolid = False

        twist_angle_rad = twist_angle * (math.pi / 180)
        twist_angleInput = adsk.core.ValueInput.createByReal(twist_angle_rad)
        sweep_input.twistAngle = twist_angleInput
        sweep_input.orientation=orientation
        # Create the surface sweep
        sweeoObj = sweeps.add(sweep_input)
        sweeoObj.bodies[0].name = name
    print(f"Surface sweep created from profile '{profile_name}' along path '{path_name}'.")

def surface_loft(profile_names,name):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent


       # Find the sketches by name
    sketch1 = None
    sketch2 = None

    for sketch in rootComp.sketches:
        if sketch.name == profile_names[0]:
            sketch1 = sketch
        elif sketch.name == profile_names[1]:
            sketch2 = sketch

    profiles = adsk.core.ObjectCollection.create()
    count = 0
    for profile_name in profile_names:
        found = False
        for sketch in rootComp.sketches:
            if sketch.name == profile_name:
                profiles.add(sketch.profiles.item(0))
                found = True
                break
        if not found:
            print(f"Error: Profile in sketch '{profile_name}' not found.")
            return
        count += 1



    # Create the loft input for a surface
    lofts = rootComp.features.loftFeatures
    loft_input = lofts.createInput(adsk.fusion.FeatureOperations.NewBodyFeatureOperation)
    loft_input.isSolid = False

    for profile in profiles:
        loft_input.loftSections.add(profile)

    # Create the surface loft
    loftObj = lofts.add(loft_input)
    loftObj.bodies[0].name = name
    print(f"Surface loft created between profiles: {', '.join(profile_names)}.")

def thicken_surfaces(body_names, thickness):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent

    # Create an ObjectCollection for the surface bodies to be thickened
    surface_bodies = adsk.core.ObjectCollection.create()

    # Find each body by name and add it to the collection
    for body_name in body_names:
        body_found = False
        for body in rootComp.bRepBodies:
            if body.name == body_name and not body.isSolid:
                surface_bodies.add(body)
                body_found = True
                break
        if not body_found:
            print(f"Error: Surface body '{body_name}' not found or is not a surface.")
            return

    if surface_bodies.count == 0:
        print("Error: No valid surface bodies found to thicken.")
        return

    # Create the thicken input for the surface bodies
    thickens = rootComp.features.thickenFeatures
    thickness_value = adsk.core.ValueInput.createByReal(thickness)
    thicken_input = thickens.createInput(surface_bodies, thickness_value, False,  adsk.fusion.FeatureOperations.NewBodyFeatureOperation)

    # Create the thicken feature
    thickens.add(thicken_input)

    print(f"Surface bodies '{', '.join(body_names)}' thickened by {thickness}.")


#arcs
def create_3_point_arc(start_point, point_on_arc, end_point, name):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent

    # Create a new sketch on the XY plane
    sketches = rootComp.sketches
    sketch = sketches.add(rootComp.xYConstructionPlane)
    sketch.name = name

    # Define the points for the arc
    start = adsk.core.Point3D.create(start_point[0], start_point[1], start_point[2])
    mid = adsk.core.Point3D.create(point_on_arc[0], point_on_arc[1], point_on_arc[2])
    end = adsk.core.Point3D.create(end_point[0], end_point[1], end_point[2])

    # Add the 3-point arc
    sketch.sketchCurves.sketchArcs.addByThreePoints(start, mid, end)

def create_start_center_sweep_arc(center_point, start_point, sweep_angle, name):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent

    # Create a new sketch on the XY plane
    sketches = rootComp.sketches
    sketch = sketches.add(rootComp.xYConstructionPlane)
    sketch.name = name

    angle_rad = sweep_angle * (math.pi / 180)

    # Define the points and angle for the arc
    center = adsk.core.Point3D.create(center_point[0], center_point[1], center_point[2])
    start = adsk.core.Point3D.create(start_point[0], start_point[1], start_point[2])


    # Add the start-center-sweep arc
    sketch.sketchCurves.sketchArcs.addByCenterStartSweep(center, start, angle_rad)




#end arcs

def hide_all_bodies():
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent
    bodies = rootComp.bRepBodies

    # Hide all bodies
    for body in bodies:
        body.isVisible = False

def rename_body(current_name, new_name):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent
    bodies = rootComp.bRepBodies

    # Find the body by its current name
    target_body = None
    for body in bodies:
        if body.name == current_name:
            target_body = body
            break

    if not target_body:
        show_message(f"Body '{current_name}' not found.")
        return

    # Rename the body
    target_body.name = new_name


def toggle_sketch_visibility(sketch_name):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent
    sketches = rootComp.sketches

    # Find the sketch by name
    target_sketch = None
    for sketch in sketches:
        if sketch.name == sketch_name:
            target_sketch = sketch
            break

    if not target_sketch:
        show_message(f"Sketch '{sketch_name}' not found.")
        return

    # Toggle the sketch's visibility
    target_sketch.isVisible = not target_sketch.isVisible

def hide_all_sketches():
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent
    sketches = rootComp.sketches

    # Hide all sketches
    for sketch in sketches:
        sketch.isVisible = False

import math

def sweep_sketch_along_path(profile_sketch_name, path_sketch_name, sweep_name, rotation_angle_deg=0.0,orientation=1):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent
    sketches = rootComp.sketches
    sweepFeats = rootComp.features.sweepFeatures

    # Find the profile sketch by name
    profile_sketch = None
    path_sketch = None
    for sketch in sketches:
        if sketch.name == profile_sketch_name:
            profile_sketch = sketch
        elif sketch.name == path_sketch_name:
            path_sketch = sketch

    if not profile_sketch or not path_sketch:
        show_message(f"Sketch '{profile_sketch_name}' or '{path_sketch_name}' not found.")
        return

    # Ensure the profile sketch has at least one profile
    if profile_sketch.profiles.count == 0:
        show_message(f"Profile sketch '{profile_sketch_name}' does not contain any profiles.")
        return

    # Get the profile from the profile sketch
    profile = profile_sketch.profiles.item(0)

    # Create the path object from curves in the path sketch
    path_curve_collection = adsk.core.ObjectCollection.create()
    for curve in path_sketch.sketchCurves:
        path_curve_collection.add(curve)

    # Create the path from the curve collection
    path = rootComp.features.createPath(path_curve_collection)

    # Set up the sweep input
    sweep_input = sweepFeats.createInput(profile, path, adsk.fusion.FeatureOperations.NewBodyFeatureOperation)

    # Convert degrees to radians for the twist angle and set it
    twist_angle_rad = rotation_angle_deg * (math.pi / 180)
    twist_angle = adsk.core.ValueInput.createByReal(twist_angle_rad)
    sweep_input.twistAngle = twist_angle
    sweep_input.orientation=orientation

    # Create the sweep feature
    sweep = sweepFeats.add(sweep_input)
    sweep.bodies[0].name = sweep_name





def loft_through_sketches(sketch_names, loft_name):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent
    sketches = rootComp.sketches
    loftFeats = rootComp.features.loftFeatures

    # Initialize a list to hold profiles for the loft
    loft_profiles = []

    # Loop through each sketch name and find the sketch
    for sketch_name in sketch_names:
        sketch = None
        for sk in sketches:
            if sk.name == sketch_name:
                sketch = sk
                break

        if not sketch:
            show_message(f"Sketch '{sketch_name}' not found.")
            return

        # Check if the sketch contains at least one profile
        if sketch.profiles.count == 0:
            show_message(f"Sketch '{sketch_name}' does not contain any profiles.")
            return

        # Add the first profile from the sketch to the loft profiles
        loft_profiles.append(sketch.profiles.item(0))

    # Ensure there are at least two profiles to create a loft
    if len(loft_profiles) < 2:
        show_message("At least two profiles are required to create a loft.")
        return

    # Create loft input and add all profiles
    loft_input = loftFeats.createInput(adsk.fusion.FeatureOperations.NewBodyFeatureOperation)
    for profile in loft_profiles:
        loft_input.loftSections.add(profile)

    # Create the loft feature
    loft = loftFeats.add(loft_input)
    loft.bodies[0].name = loft_name

def draw_shape_on_plane(plane_name, points, connections, shape_name):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent
    planes = rootComp.constructionPlanes
    sketches = rootComp.sketches

    # Find the target plane by name
    target_plane = None
    for plane in planes:
        if plane.name == plane_name:
            target_plane = plane
            break

    if not target_plane:
        show_message(f"Plane '{plane_name}' not found.")
        return

    # Create a sketch on the target plane
    sketch = sketches.add(target_plane)
    sketch.name = shape_name
    sketchLines = sketch.sketchCurves.sketchLines
    sketchArcs = sketch.sketchCurves.sketchArcs
    sketchSplines = sketch.sketchCurves.sketchFittedSplines

    # Initialize the starting point
    start_point = adsk.core.Point3D.create(points[0][0], points[0][1], points[0][2])

    # Loop over points and connections to create the segments
    for i in range(1, len(points)):
        end_point = adsk.core.Point3D.create(points[i][0], points[i][1], points[i][2])
        connection_type = connections[i - 1]

        if connection_type == 'line':
            # Create a straight line between the points
            sketchLines.addByTwoPoints(start_point, end_point)

        elif connection_type == 'curve':
            # Calculate approximate center for the arc
            center_x = (start_point.x + end_point.x) / 2
            center_y = (start_point.y + end_point.y) / 2
            center_point = adsk.core.Point3D.create(center_x, center_y, start_point.z)

            # Create the arc
            try:
                sketchArcs.addByCenterStartSweep(center_point, start_point, 1.5708)  # Quarter circle, adjust angle if needed
            except:
                # Fallback to three-point arc if center-based arc fails
                sketchArcs.addByThreePoints(start_point, adsk.core.Point3D.create(
                    (start_point.x + end_point.x) / 2,
                    (start_point.y + end_point.y) / 2,
                    start_point.z
                ), end_point)

        elif connection_type == 'g1' or connection_type == 'g2':
            # Create a spline with G1 or G2 continuity
            control_points = [start_point, end_point]
            spline = sketchSplines.add(control_points)
            if connection_type == 'g1':
                spline.isTangentContinuous = True
            elif connection_type == 'g2':
                spline.isCurvatureContinuous = True

        # Move to the next start point
        start_point = end_point

    # Optionally close the shape if needed
    if len(points) > 2 and connections[-1] == 'close':
        final_point = adsk.core.Point3D.create(points[0][0], points[0][1], points[0][2])
        sketchLines.addByTwoPoints(start_point, final_point)



def loft_between_sketches(sketch1_name, sketch2_name, loft_name):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent
    sketches = rootComp.sketches
    loftFeats = rootComp.features.loftFeatures

    # Find the sketches by name
    sketch1 = None
    sketch2 = None

    for sketch in sketches:
        if sketch.name == sketch1_name:
            sketch1 = sketch
        elif sketch.name == sketch2_name:
            sketch2 = sketch

    if not sketch1 or not sketch2:
        show_message(f"Sketch '{sketch1_name}' or '{sketch2_name}' not found.")
        return

    # Ensure both sketches have at least one profile
    if sketch1.profiles.count == 0 or sketch2.profiles.count == 0:
        show_message("One or both sketches do not contain any profiles.")
        return

    # Get the profiles for the loft
    profile1 = sketch1.profiles.item(0)
    profile2 = sketch2.profiles.item(0)

    # Create loft input and add profiles
    loft_input = loftFeats.createInput(adsk.fusion.FeatureOperations.NewBodyFeatureOperation)
    loft_input.loftSections.add(profile1)
    loft_input.loftSections.add(profile2)

    # Create the loft feature
    loft = loftFeats.add(loft_input)
    loft.bodies[0].name = loft_name



def join_bodies(body_names, result_name):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent
    bodies = rootComp.bRepBodies
    combineFeatures = rootComp.features.combineFeatures

    # Collect all specified bodies by their names
    target_body = None
    tool_bodies = adsk.core.ObjectCollection.create()

    for body in bodies:
        if body.name in body_names:
            if target_body is None:
                # Set the first body as the target
                target_body = body
            else:
                # Add all other bodies as tools
                tool_bodies.add(body)

    if not target_body or tool_bodies.count == 0:
        show_message("One or more specified bodies not found or insufficient bodies provided.")
        return

    # Create the combine feature input for the join operation
    combineInput = combineFeatures.createInput(target_body, tool_bodies)
    combineInput.isNewComponent = False
    combineInput.isKeepToolBodies = True
    combineInput.operation = adsk.fusion.FeatureOperations.JoinFeatureOperation

    # Perform the join operation
    combineFeatures.add(combineInput)

    # Rename the resulting body, if a name is provided
    target_body.name = result_name


def select_plane(plane_name):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent
    planes = rootComp.constructionPlanes
    global target_plane

    # Find the plane by name

    for plane in planes:
        if plane.name == plane_name:
            target_plane = plane
            break

    if plane_name == 'xy':
        target_plane = rootComp.xYConstructionPlane
    elif plane_name == 'xz':
        target_plane = rootComp.xZConstructionPlane
    elif plane_name == 'yz':
        target_plane = rootComp.yZConstructionPlane
    elif plane_name == 'zx':
        target_plane = rootComp.zXConstructionPlane
    elif plane_name == 'zy':
        target_plane = rootComp.zYConstructionPlane

    if not target_plane:
        show_message(f"Plane '{plane_name}' not found.")
        return


    show_message(f"Plane '{plane_name}' selected for sketching.")


def add_plane_through_points(points, plane_name):
    if len(points) != 3:
        show_message("Exactly three points are required to define the plane.")
        return

    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent
    planes = rootComp.constructionPlanes

    # Create Point3D objects for each of the three points
    pt1 = adsk.core.Point3D.create(points[0][0], points[0][1], points[0][2])
    pt2 = adsk.core.Point3D.create(points[1][0], points[1][1], points[1][2])
    pt3 = adsk.core.Point3D.create(points[2][0], points[2][1], points[2][2])


    sketches = rootComp.sketches
    sketch = sketches.add(rootComp.xYConstructionPlane)
    sketchPoints = sketch.sketchPoints
    sketchPoint1 = sketchPoints.add(pt1)
    sketchPoint2 = sketchPoints.add(pt2)
    sketchPoint3 = sketchPoints.add(pt3)
    # Create the construction plane input and define it by three points
    planeInput = planes.createInput()
    planeInput.setByThreePoints(sketchPoint1, sketchPoint2, sketchPoint3)

    # Create the plane and set its name
    plane = planes.add(planeInput)
    plane.name = plane_name


def hide_body(body_name):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent
    bodies = rootComp.bRepBodies

    # Find the body by name
    target_body = None
    for body in bodies:
        if body.name == body_name:
            target_body = body
            break

    if not target_body:
        show_message(f"Body '{body_name}' not found.")
        return

    # Set the body's visibility to False
    target_body.isVisible = False

def show_body(body_name):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent
    bodies = rootComp.bRepBodies

    # Find the body by name
    target_body = None
    for body in bodies:
        if body.name == body_name:
            target_body = body
            break

    if not target_body:
        show_message(f"Body '{body_name}' not found.")
        return

    # Set the body's visibility to True
    target_body.isVisible = True


def rotate_body(body_name, axis, angle):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent
    bodies = rootComp.bRepBodies
    moveFeats = rootComp.features.moveFeatures

    # Find the body by name
    target_body = None
    for body in bodies:
        if body.name == body_name:
            target_body = body
            break

    if not target_body:
        show_message(f"Body '{body_name}' not found.")
        return

    # Convert angle from degrees to radians
    angle_rad = angle * (3.14159265359 / 180)

    # Calculate the center of the bounding box
    bbox = target_body.boundingBox
    center_x = (bbox.minPoint.x + bbox.maxPoint.x) / 2
    center_y = (bbox.minPoint.y + bbox.maxPoint.y) / 2
    center_z = (bbox.minPoint.z + bbox.maxPoint.z) / 2
    center_point = adsk.core.Point3D.create(center_x, center_y, center_z)

    # Create the rotation matrix
    rotation_matrix = adsk.core.Matrix3D.create()
    axis_vector = None
    if axis == 'x':
        axis_vector = adsk.core.Vector3D.create(1, 0, 0)
    elif axis == 'y':
        axis_vector = adsk.core.Vector3D.create(0, 1, 0)
    elif axis == 'z':
        axis_vector = adsk.core.Vector3D.create(0, 0, 1)
    else:
        show_message("Invalid axis. Use 'x', 'y', or 'z'.")
        return

    rotation_matrix.setToRotation(angle_rad, axis_vector, center_point)

    # Apply rotation
    collection = adsk.core.ObjectCollection.create()
    collection.add(target_body)
    move_input = moveFeats.createInput(collection, rotation_matrix)
    moveFeats.add(move_input)


def scale_body(body_name, scale_factor):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent
    bodies = rootComp.bRepBodies

    # Find the body by name
    target_body = None
    for body in bodies:
        if body.name == body_name:
            target_body = body
            break


    if not target_body:
        show_message(f"Body '{body_name}' not found.")
        return


       # Create a scale input
    inputColl = adsk.core.ObjectCollection.create()
    inputColl.add(target_body)
    sketches = rootComp.sketches
    sketch = sketches.add(rootComp.xZConstructionPlane)

    bbox = target_body.boundingBox
    center_x = (bbox.minPoint.x + bbox.maxPoint.x) / 2
    center_y = (bbox.minPoint.y + bbox.maxPoint.y) / 2
    center_z = (bbox.minPoint.z + bbox.maxPoint.z) / 2
    sketchPoints = sketch.sketchPoints
    basePt = sketchPoints.add(adsk.core.Point3D.create(0, 0, 0))

    move_body(body_name, 0, 0, 0)

    scaleFactor = adsk.core.ValueInput.createByReal(scale_factor)

    scales = rootComp.features.scaleFeatures
    scaleInput = scales.createInput(inputColl, basePt, scaleFactor)

        # Set the scale to be non-uniform
    xScale = adsk.core.ValueInput.createByReal(scale_factor)
    yScale = adsk.core.ValueInput.createByReal(scale_factor)
    zScale = adsk.core.ValueInput.createByReal(scale_factor)
    scaleInput.setToNonUniform(xScale, yScale, zScale)

    scale = scales.add(scaleInput)
    move_body(body_name, center_x, center_y, center_z)


def move_body(body_name, x, y, z):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent
    bodies = rootComp.bRepBodies
    moveFeats = rootComp.features.moveFeatures

    # Find the body by name
    target_body = None
    for body in bodies:
        if body.name == body_name:
            target_body = body
            break

    if not target_body:
        show_message(f"Body '{body_name}' not found.")
        return

    # Get the bounding box and calculate its center
    bbox = target_body.boundingBox
    current_center_x = (bbox.minPoint.x + bbox.maxPoint.x) / 2
    current_center_y = (bbox.minPoint.y + bbox.maxPoint.y) / 2
    current_center_z = (bbox.minPoint.z + bbox.maxPoint.z) / 2

    # Calculate the translation vector
    dx = x - current_center_x
    dy = y - current_center_y
    dz = z - current_center_z

    # Create a translation matrix
    translation_matrix = adsk.core.Matrix3D.create()
    translation_matrix.translation = adsk.core.Vector3D.create(dx, dy, dz)

    # Set up and apply the move feature
    collection = adsk.core.ObjectCollection.create()
    collection.add(target_body)
    move_input = moveFeats.createInput(collection, translation_matrix)
    moveFeats.add(move_input)

def cut(body1_name, body2_name):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent
    bodies = rootComp.bRepBodies
    combineFeatures = rootComp.features.combineFeatures

    # Find body1 and body2 by name
    body1 = None
    tool_bodies = adsk.core.ObjectCollection.create()

    for body in bodies:
        if body.name == body1_name:
            body1 = body
        elif body.name == body2_name:
            tool_bodies.add(body)

    if not body1 or  tool_bodies.count==0:
        show_message(f"Body '{body1_name}' or '{body2_name}' not found.")
        return

    # Set up the combine input for cutting
    combineInput = combineFeatures.createInput(body1, tool_bodies)
    combineInput.isNewComponent = False
    combineInput.isKeepToolBodies = True
    combineInput.operation = adsk.fusion.FeatureOperations.CutFeatureOperation
    combineFeatures.add(combineInput)



def duplicate_body(body_name, x, y, z, new_name):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent
    bodies = rootComp.bRepBodies
    moveFeats = rootComp.features.moveFeatures

    # Find the body by name
    original_body = None
    for body in bodies:
        if body.name == body_name:
            original_body = body
            break

    if not original_body:
        show_message(f"Body '{body_name}' not found.")
        return

    # Duplicate the body
    new_body = original_body.copyToComponent(rootComp)
    new_body.name = new_name
    move_body(new_body.name, x, y, z)




def export_design(file_path):
    app = adsk.core.Application.get()
    design = app.activeProduct
    exportMgr = design.exportManager

    # Set up the export options for OBJ format
    objOptions = exportMgr.createOBJExportOptions(design.rootComponent, file_path)

    # Perform the export
    exportMgr.execute(objOptions)
    show_message(f'Exported design to {file_path}')

def run_server():
    global httpd
    server_address = ('', 8001)
    httpd = HTTPServer(server_address, RequestHandler)
    httpd.serve_forever()

def show_message(message):
    app = adsk.core.Application.get()
    ui  = app.userInterface
    ui.messageBox(message)

def draw_point(x, y, z):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent
    sketches = rootComp.sketches
    global target_plane
    plane = target_plane
    if not plane:
        plane = rootComp.xYConstructionPlane
    sketch = sketches.add(plane)
    sketchPoints = sketch.sketchPoints
    point = sketchPoints.add(adsk.core.Point3D.create(x, y, z))
    return point.worldGeometry

def draw_points(points):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent
    sketches = rootComp.sketches
    global target_plane
    plane = target_plane
    if not plane:
        plane = rootComp.xYConstructionPlane
    sketch = sketches.add(plane)
    sketchPoints = sketch.sketchPoints

    # Loop through each point in the list and add it to the sketch
    for point in points:
        x, y, z = point
        sketchPoints.add(adsk.core.Point3D.create(x, y, z))

    print(f"{len(points)} points added to sketch on XY plane.")

def draw_line(pt1, pt2, name):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent
    sketches = rootComp.sketches
    global target_plane
    plane = target_plane
    if not plane:
        plane = rootComp.xYConstructionPlane
    sketch = sketches.add(plane)
    sketchLines = sketch.sketchCurves.sketchLines
    point1 = adsk.core.Point3D.create(pt1[0], pt1[1], pt1[2])
    point2 = adsk.core.Point3D.create(pt2[0], pt2[1], pt2[2])
    line = sketchLines.addByTwoPoints(point1, point2)
    sketch.name = name

def draw_circle(pt1, radius, name):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent
    sketches = rootComp.sketches
    global target_plane

    plane = target_plane
    if not plane:
        plane = rootComp.xYConstructionPlane
    sketch = sketches.add(plane)
    centerPoint = adsk.core.Point3D.create(pt1[0], pt1[1], pt1[2])
    circle = sketch.sketchCurves.sketchCircles.addByCenterRadius(centerPoint, radius)
    sketch.name = name

def draw_rectangle(pt1, pt2, name):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent
    sketches = rootComp.sketches
    global target_plane
    plane = target_plane
    if not plane:
        plane = rootComp.xYConstructionPlane
    sketch = sketches.add(plane)
    point1 = adsk.core.Point3D.create(pt1[0], pt1[1], pt1[2])
    point2 = adsk.core.Point3D.create(pt2[0], pt2[1], pt2[2])
    rectangle = sketch.sketchCurves.sketchLines.addTwoPointRectangle(point1, point2)
    sketch.name=name

def draw_centered_rectangle(pt1, width, height, name):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent
    sketches = rootComp.sketches
    global target_plane
    plane = target_plane
    if not plane:
        plane = rootComp.xYConstructionPlane
    sketch = sketches.add(plane)
    cx, cy, cz = pt1[0], pt1[1], pt1[2]
    point1 = adsk.core.Point3D.create(cx - width / 2, cy - height / 2, cz)
    point2 = adsk.core.Point3D.create(cx + width / 2, cy + height / 2, cz)
    rectangle = sketch.sketchCurves.sketchLines.addTwoPointRectangle(point1, point2)
    sketch.name=name

def add_offset_plane(base_plane_name, offset, plane_name):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent
    planes = rootComp.constructionPlanes

    target_plane = None
    for plane in planes:
        if plane.name == base_plane_name:
            target_plane = plane
            break

    base_plane = None
    if target_plane is not None:
        base_plane = target_plane
    if base_plane_name == "xy":
        base_plane = rootComp.xYConstructionPlane
    elif base_plane_name == "yz":
        base_plane = rootComp.yZConstructionPlane
    elif base_plane_name == "xz":
        base_plane = rootComp.xZConstructionPlane
    else:
        if base_plane is None:
           show_message("Invalid base plane name. Use 'xy', 'yz', or 'xz' or name.")
           return

    # Create the offset input
    offset_value = adsk.core.ValueInput.createByReal(offset)
    plane_input = planes.createInput()
    plane_input.setByOffset(base_plane, offset_value)

    # Add the offset plane
    offset_plane = planes.add(plane_input)
    offset_plane.name = plane_name



def revolve_shape(name, axis_index, angle,bodyName):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent
    revolves = rootComp.features.revolveFeatures

    # Find the sketch by name
    sketch = None
    for sk in rootComp.sketches:
        if sk.name == name:
            sketch = sk
            break

    if not sketch:
        show_message(f'Sketch "{name}" not found')
        return

    # Choose axis based on index (0: X, 1: Y, 2: Z)
    if axis_index == 0:
        axis = rootComp.xConstructionAxis
    elif axis_index == 1:
        axis = rootComp.yConstructionAxis
    elif axis_index == 2:
        axis = rootComp.zConstructionAxis
    else:
        show_message(f'Invalid axis index {axis_index}. Use 0 for X, 1 for Y, or 2 for Z.')
        return

    # Prepare the revolve input
    profile = sketch.profiles.item(0)
    revInput = revolves.createInput(profile, axis, adsk.fusion.FeatureOperations.NewBodyFeatureOperation)
    angleValue = adsk.core.ValueInput.createByReal(angle * (3.14159265359 / 180))  # Convert degrees to radians
    revInput.setAngleExtent(False, angleValue)

    # Create the revolve feature
    extrudeObj = revolves.add(revInput)
    extrudeObj.bodies[0].name = bodyName

def extrude_shape(name, offset, taper_angle,bodyName):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent
    extrudes = rootComp.features.extrudeFeatures

    # Find the profile by name
    for sketch in rootComp.sketches:
        if sketch.name == name:
            extInput = extrudes.createInput(sketch.profiles.item(0), adsk.fusion.FeatureOperations.NewBodyFeatureOperation)

            # Set the distance for the extrusion
            distance = adsk.core.ValueInput.createByReal(offset)
            extInput.setDistanceExtent(False, distance)

    # Convert degrees to radians for the twist angle and set it
            taper_angle_rad = taper_angle * (math.pi / 180)
            # Set the taper angle for the extrusion
            angle = adsk.core.ValueInput.createByReal(taper_angle_rad)
            extInput.taperAngle = angle

            # Create the extrude feature
            extrudeObj = extrudes.add(extInput)
            extrudeObj.bodies[0].name = bodyName
            return

    show_message(f'Shape "{name}" not found')

def add_text(text_content, plane_name, text_position1,text_position2, font_size=1.0):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent
    sketches = rootComp.sketches
    planes = rootComp.constructionPlanes

    # Convert the plane_name to lowercase for case-insensitive matching
    plane_name = plane_name.lower()

    # Find the target plane by name
    target_plane = None

    # Check custom construction planes first
    for plane in planes:
        if plane.name.lower() == plane_name:
            target_plane = plane
            break

    # If not found, check if it is one of the standard base planes
    if not target_plane:
        if plane_name == "xy":
            target_plane = rootComp.xYConstructionPlane
        elif plane_name == "yz":
            target_plane = rootComp.yZConstructionPlane
        elif plane_name == "xz":
            target_plane = rootComp.xZConstructionPlane
        else:
            show_message(f"Plane '{plane_name}' not found. Please specify 'xy', 'yz', 'xz', or a custom plane name.")
            return

    # Create a sketch on the target plane
    sketch = sketches.add(target_plane)

    # Define the position for the text
    text_position11 = adsk.core.Point3D.create(text_position1[0], text_position1[1], text_position1[2])
    text_position22 = adsk.core.Point3D.create(text_position2[0], text_position2[1], text_position2[2])

    # Add text to the sketch
    text_sketch_texts = sketch.sketchTexts
    text_input = text_sketch_texts.createInput2(text_content, font_size)  # Use createInput2 for font size support

    # Set the text with the required arguments only
    text_input.setAsMultiLine(text_position11,text_position22,
                             adsk.core.HorizontalAlignments.LeftHorizontalAlignment,
                             adsk.core.VerticalAlignments.TopVerticalAlignment, 0)

    # Add the text to the sketch
    text_sketch_texts.add(text_input)

def draw_fitted_spline(points,name):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent

    # Create a new sketch on the XY plane (or any other desired plane)
    sketches = rootComp.sketches
    global target_plane
    plane = target_plane
    if not plane:
        plane = rootComp.xYConstructionPlane
    sketch = sketches.add(plane)

    sketch.name= name

    # Create a collection of points for the spline
    spline_points = adsk.core.ObjectCollection.create()
    for point in points:
        spline_point = adsk.core.Point3D.create(point[0], point[1], point[2])
        spline_points.add(spline_point)

    # Add the fitted spline through the points
    sketch.sketchCurves.sketchFittedSplines.add(spline_points)

def draw_conic_curve(start_point, end_point,apex_point,rho_value,name):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent

    # Create a new sketch on the XY plane (or any other desired plane)
    sketches = rootComp.sketches
    global target_plane
    plane = target_plane
    if not plane:
        plane = rootComp.xYConstructionPlane
    sketch = sketches.add(plane)

    sketch.name= name

    # Define points for the conic curve
    start = adsk.core.Point3D.create(start_point[0], start_point[1], start_point[2])
    end = adsk.core.Point3D.create(end_point[0], end_point[1], end_point[2])
    apex = adsk.core.Point3D.create(apex_point[0], apex_point[1], apex_point[2])


    # Add the conic curve to the sketch
    sketch.sketchCurves.sketchConicCurves.add(start, end,apex, rho_value)

def draw_curves(name, paths,count):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent
    sketches = rootComp.sketches

    global target_plane
    plane = target_plane
    if not plane:
        plane = rootComp.xYConstructionPlane
    sketch = sketches.add(plane)

    sketchLines = sketch.sketchCurves.sketchLines
    sketch.name= name
    for i in range(count):
       path = paths[i]
       if(path["mode"]=="Line"):
          #iterate over path points and draw lines
            for j in range(len(path["points"]) - 1):
                pt1 = path["points"][j]
                pt2 = path["points"][j + 1]
                point1 = adsk.core.Point3D.create(pt1["x"], pt1["y"], pt1["z"])
                point2 = adsk.core.Point3D.create(pt2["x"], pt2["y"], pt2["z"])
                line = sketchLines.addByTwoPoints(point1, point2)
       elif(path["mode"]=="Spline"):
            spline_points = adsk.core.ObjectCollection.create()
            for point in path["points"]:
                spline_point = adsk.core.Point3D.create(point["x"], point["y"], point["z"])
                spline_points.add(spline_point)
            sketch.sketchCurves.sketchFittedSplines.add(spline_points)


def draw_lines_pair(points,points2, name):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent
    sketches = rootComp.sketches

    global target_plane
    plane = target_plane
    if not plane:
        plane = rootComp.xYConstructionPlane
    sketch = sketches.add(plane)

    sketchLines = sketch.sketchCurves.sketchLines
    sketch.name= name
    v1 = len(points)
    v2 = len(points2)
    length = v1
    if v1<v2:
        length = v2
    for i in range(length ):
        pt1 = points[i]
        pt2 = points2[i]
        point1 = adsk.core.Point3D.create(pt1[0], pt1[1], pt1[2])
        point2 = adsk.core.Point3D.create(pt2[0], pt2[1], pt2[2])
        line = sketchLines.addByTwoPoints(point1, point2)


def draw_lines(points, name, close):
    app = adsk.core.Application.get()
    design = app.activeProduct
    rootComp = design.rootComponent
    sketches = rootComp.sketches

    global target_plane
    plane = target_plane
    if not plane:
        plane = rootComp.xYConstructionPlane
    sketch = sketches.add(plane)

    sketchLines = sketch.sketchCurves.sketchLines
    sketch.name= name
    for i in range(len(points) - 1):
        pt1 = points[i]
        pt2 = points[i + 1]
        point1 = adsk.core.Point3D.create(pt1[0], pt1[1], pt1[2])
        point2 = adsk.core.Point3D.create(pt2[0], pt2[1], pt2[2])
        line = sketchLines.addByTwoPoints(point1, point2)

    if close:
        pt1 = points[-1]
        pt2 = points[0]
        point1 = adsk.core.Point3D.create(pt1[0], pt1[1], pt1[2])
        point2 = adsk.core.Point3D.create(pt2[0], pt2[1], pt2[2])
        line = sketchLines.addByTwoPoints(point1, point2)



def run(context):
    ui = None
    try:
        app = adsk.core.Application.get()
        ui  = app.userInterface

        # Start the web server in a new thread
        server_thread = threading.Thread(target=run_server)
        server_thread.daemon = True
        server_thread.start()

        ui.messageBox('v5 Web server started on port 8000. Send POST requests to http://localhost:8000')

    except Exception as e:
        if ui:
            ui.messageBox('Failed:\n{}'.format(traceback.format_exc()))

# This is required if running in the Fusion 360 script editor
