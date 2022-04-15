package tests

import util.{Camera, Function3D, ImageMod, Math3D}

import java.awt.Graphics
import java.awt.event.{KeyEvent, KeyListener}
import java.awt.image.{BufferStrategy, BufferedImage}
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JFrame

object JFrameTest extends KeyListener {

  final val windowSize: Array[Int] = Array(800, 800)
  final val resolution: Array[Int] = Array(400, 400)


  var img: BufferedImage = null

  def init(): Unit = {
    try {
      img = ImageIO.read(new File("ref/rosie stays warm.jpg"))
    } catch {
      case e: Exception => println("couldn't load img")
    }
  }

  def main(args: Array[String]): Unit = {
    val jf: JFrame = new JFrame()
    jf.setTitle("3d")
    jf.setSize(windowSize(0), windowSize(1));
    jf.setResizable(false)
    jf.setDefaultCloseOperation(3)
    jf.setLocationRelativeTo(null)
    jf.setVisible(true);
    jf.addKeyListener(this)

    init()

    while (true) {
      update()
      render(jf)
      try {
        Thread.sleep(100)
      }
    }
  }

  val cam: Camera = new Camera()
  cam.position(0) = 0f
  val cubes: Array[Array[Array[Float]]] = Array(
    util.Objects3D.createCube(1, 0, -1, 1),
    util.Objects3D.createCube(5, 5, 1, 1),
    util.Objects3D.createCube(-2, 0, -1, 1),
    util.Objects3D.createCube(-5, 1, -1, 1))
  val boundingBox: Array[Array[Float]] = util.Objects3D.createCube(-15, -15, -15, 30)
  val point: Array[Float] = Array[Float](0f, 0f, 0f)
  val quad: Array[Array[Float]] = util.Objects3D.square3D(0, 0, 0, 5)

  val f: Function3D = new Function3D { //convert to arc length parameterization
    override def function2D(s: Double): Array[Double] = {
      Array(s, s)
    }

    override def planeFunction(t: Double, u: Double): Array[Double] = {
      Array(
        /*10*Math.sin(t * Math.PI/180),
        10*Math.cos(t * Math.PI/180) * Math.cos(u * Math.PI/180),
        10*Math.cos(t * Math.PI/180) * Math.sin(u * Math.PI/180),*/
        t * t,
        5 * Math.cos(u),
        5 * Math.sin(u),
      )
    }
  }


  var angle: Float = 0;

  def update(): Unit = {
    //cam.position(0)+=0.1f
    //println(cam.position(0))
    canvas.setRGB(0, 0, canvas.getWidth(), canvas.getHeight(), blank, 0, canvas.getWidth())

    //draw axis
    try {
      ImageMod.drawQuickLine(canvas, cam.projectToCanvas(canvas, cam.project(Array(-20, 0, 10))), cam.projectToCanvas(canvas, cam.project(Array(20, 0, 10))), 255 << 16 | 255 << 8 | 255)
    } catch {
      case e: Exception =>
    }
    try {
      ImageMod.drawQuickLine(canvas, cam.projectToCanvas(canvas, cam.project(Array(0, -20, 10))), cam.projectToCanvas(canvas, cam.project(Array(0, 20, 10))), 255 << 16 | 255 << 8 | 255)
    } catch {
      case e: Exception =>
    }
    try {
      ImageMod.drawQuickLine(canvas, cam.projectToCanvas(canvas, cam.project(Array(0, 0, 15))), cam.projectToCanvas(canvas, cam.project(Array(0, 0, 5))), 255 << 16 | 255 << 8 | 255)
    } catch {
      case e: Exception =>
    }

    var points: List[Array[Float]] = List()
    for (i <- -1000 to 1000) {
      val j: Double = (i: Double) / 100
      points = f.function2DtoPlane(j) :: points
      /*try {
            //TODO: calculate float[]s and order them into array or linked based on distance, then render through ordered structure
      }catch{
        case e: Exception =>
      }*/
    }

    points = points.sortWith((i:Array[Float], j:Array[Float])=>{
      Math3D.distanceBetweenSquared(cam.position, i) > Math3D.distanceBetweenSquared(cam.position, j)
    })
    /*points.foreach((i:Array[Float])=>{
    print(Math3D.distanceBetweenSquared(cam.position, i) + ", ")
    })
    println()*/

    var ColorC: Int = 0
    for(i <- points){
      try {
        val point: Array[Int] = cam.projectToCanvas(canvas, cam.project(i))
        ImageMod.fillRect(canvas, point(0), point(1), 2, 2, 255 << 16 | (255 - ColorC/4) << 8 | (255 - ColorC/4))
      }catch{case e: Exception =>}

      ColorC+=1
    }
    //things in the distance don't have to be ordered before they're rendered

    //f.a = Array(Math.cos(angle).toFloat, Math.sin(angle).toFloat, 0)
    //f.b = Array(Math.sin(angle).toFloat, Math.cos(angle).toFloat, 0)

    //angle+=0.1f

    try {
      //ImageMod.drawImage(img, canvas, cam.projectToCanvas(canvas, cam.project(quad(0))), cam.projectToCanvas(canvas, cam.project(quad(1))), cam.projectToCanvas(canvas, cam.project(quad(2))), cam.projectToCanvas(canvas, cam.project(quad(3))))
    } catch {
      case e: Exception => println("")
    }
    //ImageMod.drawQuickLine(canvas, Array(50, 50), Array(25, 75), 255 << 16 | 255 << 8 | 255)
    /*val insct: Array[Float] = cam.project(point)
    printArray(insct)
    println(Math3D.magnitude(Math3D.addVectors(point, Math3D.addVectors(cam.PN, cam.position, 1), -1)))
    canvas.setRGB(point(0).toInt + 50, point(1).toInt + 50, 255 << 16 | 255 << 8 | 255)
    if (insct != null) {
      canvas.setRGB((insct(0) * 1).toInt + 200, insct(1).toInt + 200, 255 << 16 | 255 << 8 | 255)
    }*/
    for (k <- 0 to cubes.length - 1) {
      for (h <- 0 to cubes(k).length /*8*/ ) {
        //val v: Array[Float] = cubes(k)(h)
        val n: Array[Float] = cam.project(cubes(k)(h % cubes(k).length))
        val n2: Array[Float] = cam.project(cubes(k)(Math.abs((h + 1) % (cubes(k).length))))

        if (n != null && n2 != null) {
          //printArray(cam.NV)
          /*for (i <- 0 to 1) {
            for (j <- 0 to 1) {
              if(n(0).toInt + i + 200 >= 0 && n(0).toInt + i + 200 < 400 && n(1).toInt + j + 200 >= 0 && n(1).toInt + j + 200 < 400) {
                canvas.setRGB(n(0).toInt + i + 200, n(1).toInt + j + 200, 255 << 16 | 255 << 8 | 255)
              }
              //canvas.setRGB(i, j, i*10 % 255 << 16 | j % 255 << 8 | 0)
            }
          }*/

          //ImageMod.fillRect(canvas, n(0).toInt + 200, n(1).toInt + 200, 10, 10, 255 << 16 | 255 << 8 | 255)
          //ImageMod.drawQuickLine(canvas, cam.projectToCanvas(canvas, n), cam.projectToCanvas(canvas, n2), 255 << 16 | 255 << 8 | 255)


          //println(canvas)
          //printArray(n)
        }
      }
    }
  }

  val canvas: BufferedImage = new BufferedImage(resolution(0), resolution(1), BufferedImage.TYPE_INT_RGB)
  val blank: Array[Int] = new Array[Int](resolution(0) * resolution(1))

  def render(jf: JFrame): Unit = {
    val bs: BufferStrategy = jf.getBufferStrategy()
    if (bs == null) {
      jf.createBufferStrategy(3)
      return
    }
    val g: Graphics = bs.getDrawGraphics()
    g.drawImage(canvas, 0, 0, windowSize(0), windowSize(1), null)
    //g.setColor(Color.white)
    //g.drawRect(cam.position(0).toInt - 2 + 50, cam.position(1).toInt - 2 + 50, 4, 4)
    //g.drawLine(cam.position(0).toInt + 50, cam.position(1).toInt + 50, (cam.position(0) + 50 + cam.PN(0)*1).toInt, (cam.position(1).toInt + 50 + cam.PN(1)*1).toInt)
    //g.drawLine((cam.position(0) + 50 + cam.PN(0)*1).toInt, (cam.position(1).toInt + 50 + cam.PN(1)*1).toInt, (cam.position(0) + 50 + cam.PN(0)*1).toInt + (cam.NH(0)*10).toInt, (cam.position(1).toInt + 50 + cam.PN(1)*1).toInt + (cam.NH(1)*10).toInt)
    //g.drawRect(cubes(0)(0)(0).toInt + 45, cubes(0)(0)(1).toInt + 45, 10, 10)
    bs.show()
  }

  def printArray[A](a: Array[A]): Unit = {
    if (a == null) {
      return
    }
    print("< ")
    for (i <- a) {
      print(i.toString + ", ")
    }
    println(">")
  }

  override def keyPressed(e: KeyEvent): Unit = {
    //println(e)
    if (e.getKeyCode == 87) {
      cam.position = Math3D.addVectors(cam.position, Math3D.scalarMult(cam.getLooking(), 1), 1f)
    }
    if (e.getKeyCode == 83) {
      cam.position = Math3D.addVectors(cam.position, Math3D.scalarMult(cam.getLooking(), 1), -1f);
    }
    if (e.getKeyCode == 65) {
      cam.position = Math3D.addVectors(cam.position, Math3D.scalarMult(cam.NH, 1), -1f)
    }
    if (e.getKeyCode == 68) {
      cam.position = Math3D.addVectors(cam.position, Math3D.scalarMult(cam.NH, 1), 1f);
    }
    if (e.getKeyCode == 37) {
      //cam.position(1) = cam.position(1) + 1f;
      cam.yaw(0.1f)
    }
    if (e.getKeyCode == 39) {
      //cam.position(1) = cam.position(1) - 1f;
      cam.yaw(-0.1f)
    }
    if (e.getKeyCode == 38) {
      //cam.position(1) = cam.position(1) - 1f;
      cam.pitch(-0.1f)
    }
    if (e.getKeyCode == 40) {
      cam.pitch(0.1f)
    }

    if (e.getKeyCode == 88) {
      cam.position = Math3D.addVectors(cam.position, Array(0, 0, 1), -1)
    }
    if (e.getKeyCode == 90) {
      cam.position = Math3D.addVectors(cam.position, Array(0, 0, 1), 1)
    }
  }

  override def keyTyped(e: KeyEvent): Unit = {


  }

  override def keyReleased(e: KeyEvent): Unit = {


  }

}
