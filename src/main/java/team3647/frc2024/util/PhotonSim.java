package team3647.frc2024.util;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import org.photonvision.PhotonCamera;
import org.photonvision.estimation.TargetModel;
import org.photonvision.simulation.PhotonCameraSim;
import org.photonvision.simulation.SimCameraProperties;
import org.photonvision.simulation.VisionSystemSim;
import org.photonvision.simulation.VisionTargetSim;

import edu.wpi.first.apriltag.AprilTag;
import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Rotation2d;

public class PhotonSim {
    public VisionSystemSim system;
    List<VisionTargetSim> targets;
    AprilTagFieldLayout layout;

    AprilTagPhotonVision backLeft,backRight,left,right,zoom; 
    
    
    public PhotonSim(AprilTagPhotonVision backleft, AprilTagPhotonVision backRight, AprilTagPhotonVision left, AprilTagPhotonVision right, AprilTagPhotonVision zoom){
        this.backLeft = backleft;
        this.backRight = backRight;
        this.left = left;
        this.right = right;
        this.zoom = zoom;
        this.layout = AprilTagFieldLayout.loadField(AprilTagFields.k2024Crescendo);
        this.system = new VisionSystemSim("main");
        this.targets = new ArrayList<VisionTargetSim>();

        addTargets();
        addCams();

        VisionTargetSim[] arr = new VisionTargetSim[targets.size()];
        this.system.addVisionTargets(targets.toArray(arr));

        system.addAprilTags(layout);
            
        

    }

    public void addTargets(){
        for(AprilTag tag : layout.getTags()){
            var target = new VisionTargetSim(tag.pose, TargetModel.kAprilTag36h11, tag.ID);
            targets.add(target);
            
        }
    }


    public void addCams(){
        var camProps = new SimCameraProperties();
        camProps.setCalibration(1280, 720, Rotation2d.fromDegrees(120));
        camProps.setCalibError(0, 0);
        camProps.setFPS(50);
        camProps.setAvgLatencyMs(35);
        camProps.setLatencyStdDevMs(5);
        
        system.addCamera(new PhotonCameraSim(backLeft, camProps), backLeft.robotToCam);
        system.addCamera(new PhotonCameraSim(backRight, camProps), backRight.robotToCam);
        system.addCamera(new PhotonCameraSim(left, camProps), left.robotToCam);
        system.addCamera(new PhotonCameraSim(right, camProps), right.robotToCam);
        // system.addCamera(new PhotonCameraSim(zoom, camProps), zoom.robotToCam);
    }
}
