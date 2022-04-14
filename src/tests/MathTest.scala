package tests
import util._
import tests.JFrameTest
import util.{Camera, ImageMod, Math3D}

object MathTest {

  def main(args: Array[String]): Unit = {
    val cam: Camera = new Camera()
    cam.position = Array(-324, 0, 0)
    //JFrameTest.printArray(Math3D.crossProduct(cam.position, Array[Float](-0.0, 1, -10)))
    //println(Math3D.dotProduct(cam.position, Array[Float](0, 1, 2)))
    //JFrameTest.printArray(Math3D.scalarMult(cam.position, 0.06))
    val v1 = cam.project(Array[Float](2, 2, 0))
    JFrameTest.printArray(v1)
    println(Math3D.percent(1,-1,-2))
    println(Math3D.percent_inverse(0.8, -10, -20))
    println(Math3D.lerp(-4, -2, -6, 10, 20))

    val f: Function3D = new Function3D{ //convert to arc length parameterization
      override def function2D(s: Double): Array[Double] = {
        Array(s, s)
      }

      override def planeFunction(t: Double, u: Double): Array[Double] = {
        Array(
          t,
          100*Math.cos(u * Math.PI/180),
          100*Math.sin(u * Math.PI/180),
        )
      }
    }

    JFrameTest.printArray(f.function2DtoPlane(90))
  }
}
