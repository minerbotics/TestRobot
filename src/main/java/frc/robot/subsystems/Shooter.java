package frc.robot.subsystems;

import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ShooterConstants;

public class Shooter extends SubsystemBase {
  private final CANSparkMax m_TopShooterMotor, m_MidShooterMotor, m_TopRightShooterMotor, m_TopLeftShooterMotor;

  public Shooter() {
    m_TopShooterMotor = new CANSparkMax(ShooterConstants.TOP_SHOOTER_MOTOR, CANSparkLowLevel.MotorType.kBrushless);
    m_MidShooterMotor = new CANSparkMax(ShooterConstants.MID_SHOOTER_MOTOR, CANSparkLowLevel.MotorType.kBrushless);
    m_TopRightShooterMotor = new CANSparkMax(ShooterConstants.TOP_RIGHT_SHOOTER_MOTOR, CANSparkLowLevel.MotorType.kBrushless);
    m_TopLeftShooterMotor = new CANSparkMax(ShooterConstants.TOP_LEFT_SHOOTER_MOTOR, CANSparkLowLevel.MotorType.kBrushless);
    m_TopRightShooterMotor.follow(m_TopLeftShooterMotor, true);
  }

  public void ampOut() {
    m_TopRightShooterMotor.set(0.25);
    m_MidShooterMotor.set(0.25);
  }

  public void speakerOut() {
    m_TopShooterMotor.set(-1);
    m_TopLeftShooterMotor.set(-1);
    m_MidShooterMotor.set(-0.25);
    m_TopRightShooterMotor.set(-0.25);
  }

  public void intakeIn() {
    m_TopRightShooterMotor.set(-0.5);
    m_MidShooterMotor.set(0);
    m_TopShooterMotor.set(0);
  }

  public void spinTop() {
    m_TopShooterMotor.set(-1);
    m_TopLeftShooterMotor.set(-1);
  }

  public void stop() {
    m_TopRightShooterMotor.set(0);
    m_MidShooterMotor.set(0);
    m_TopShooterMotor.set(0);
    m_TopLeftShooterMotor.set(0);
  }
}
