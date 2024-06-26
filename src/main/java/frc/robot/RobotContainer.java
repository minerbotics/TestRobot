// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.AutoDrive;
import frc.robot.commands.DefaultDriveCommand;
import frc.robot.subsystems.Swerve;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  // Subsystems
  private final Swerve m_Swerve;


  private final CommandXboxController m_driverController;

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Subsystems
    m_Swerve = new Swerve();

    // Controllers
    m_driverController = new CommandXboxController(OperatorConstants.DRIVER_CONTROLLER_PORT);
    
    // Commands
//    NamedCommands.registerCommand("AmpShoot", new Shoot(m_Swerve, null, null, null, GoalTypeConstants.AMP));
    
    // The controls are for field-oriented driving:
    // Left stick Y axis -> forward and backwards movement
    // Left stick X axis -> left and right movement
    // Right stick X axis -> rotation
    m_Swerve.setDefaultCommand(new DefaultDriveCommand(
            m_Swerve,
            () -> -modifyAxis(m_driverController.getLeftY()) * Swerve.MAX_VELOCITY_METERS_PER_SECOND,
            () -> -modifyAxis(m_driverController.getLeftX()) * Swerve.MAX_VELOCITY_METERS_PER_SECOND,
            () -> -modifyAxis(m_driverController.getRightX()) * Swerve.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND
    ));

    SmartDashboard.updateValues();
    // Configure the trigger bindings
    configureBindings();

  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    
    m_driverController.start()
      .onTrue(new InstantCommand(() -> m_Swerve.zeroGyroscope()));

    /** DPAD commands for robot centric slow movement */
    m_driverController.povUp()
      .whileTrue(new DefaultDriveCommand(m_Swerve, () -> 0.5, () -> 0.0, () -> 0.0, true));
    m_driverController.povDown()
      .whileTrue(new DefaultDriveCommand(m_Swerve, () -> -0.5, () -> 0.0, () -> 0.0, true));
    m_driverController.povLeft()
      .whileTrue(new DefaultDriveCommand(m_Swerve, () -> 0.0, () -> 0.5, () -> 0.0, true));
    m_driverController.povRight()
      .whileTrue(new DefaultDriveCommand(m_Swerve, () -> 0.0, () -> -0.5, () -> 0.0, true));
    
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   * 
   * @return the command to run in autonomous
   */
   public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return new AutoDrive(m_Swerve, new ChassisSpeeds(0, 0, 0));
  }

  private static double deadband(double value, double deadband) {
    if (Math.abs(value) > deadband) {
      if (value > 0.0) {
        return (value - deadband) / (1.0 - deadband);
      } else {
        return (value + deadband) / (1.0 - deadband);
      }
    } else {
      return 0.0;
    }
  }

  private static double modifyAxis(double value) {
    // Deadband
    value = deadband(value, 0.05);

    // Square the axis
    value = Math.copySign(value * value, value);

    return value;
  }



}
