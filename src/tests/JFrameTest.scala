package tests

import primitives.{Camera, Mesh, Scene3D, Trigon}
import util.{Function3D, ImageMod, Math3D, Objects3D}

import java.awt.Graphics
import java.awt.event.{KeyEvent, KeyListener}
import java.awt.image.{BufferStrategy, BufferedImage}
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JFrame

/**
 * Graphics Renderer Demo
 * Move Camera with WASD
 * Rotate Camera with Arrow Keys
 */
object JFrameTest extends KeyListener {

  final val windowSize: Array[Int] = Array(800, 800)
  final val resolution: Array[Int] = Array(400, 400)//256, 224


  var img: BufferedImage = null

  var scene1:Scene3D = null
  var scene2: Scene3D = null
  var activeScene: Scene3D = null

  def init(): Unit = {
    try {
      img = ImageIO.read(new File("ref/rosie stays warm.jpg"))
    } catch {
      case e: Exception => println("couldn't load img")
    }

    var i: Int = 0
    while(i < blank.length){
      if(Math.random() > 0.994){
        blank(i) = 255 << 16 | 255 << 8 | 255
      }else{
        blank(i) = 13 << 16 | 3 << 8 | 90
      }
      i+=1
    }

    scene1 = new Scene3D(){
      override def initialize(): Array[Float] = {
        activeCamera = new Camera()
        functions = Array(
          new Function3D { //convert to arc length parameterization
            offset = Array(0, 0, -20)
            a = Array(10, 0, 0)
            override def function2D(s: Double): Array[Double] = {
              Array(0.05*s, 100*s)
            }

            override def planeFunction(t: Double, u: Double): Array[Double] = {
              Array(
                /*10*Math.sin(t * Math.PI/180),
                10*Math.cos(t * Math.PI/180) * Math.cos(u * Math.PI/180),
                10*Math.cos(t * Math.PI/180) * Math.sin(u * Math.PI/180),*/
                t,
                2 * Math.cos(u),
                2 * Math.sin(u),
              )
            }
          }
        )
        Array(0)
      }
      override def update(): Array[Float] = {
        for (f <- functions) {
          //f.a = Array(Math.cos(angle).toFloat, Math.sin(angle).toFloat, 0)
          f.a = Math3D.rotateAbout(f.a, Array(0, 1, 1.6f), 0.1)
          //f.b = Array(-Math.sin(angle).toFloat, Math.cos(angle).toFloat, 0)
          f.b = Math3D.rotateAbout(f.b, Array(0, 1, 1.6f), 0.1)
          f.b = Math3D.rotateAbout(f.b, f.a, 0.1)
          //f.c = Array(Math.sin(angle + Math.PI/2).toFloat, 0, Math.cos(angle + Math.PI/2).toFloat)
          f.c = Math3D.rotateAbout(f.c, Array(0, 1, 1.6f), 0.1)
          f.c = Math3D.rotateAbout(f.c, f.a, 0.1)

          //f.offset = Array(5*Math.cos(angle).toFloat, 5*Math.sin(-angle).toFloat, -2)
          //angle = angle + 0.1f
          f.offset = Math3D.addVectors(f.offset, Math3D.scalarMult(f.a, 1 / Math3D.magnitude(f.a)), 1)
        }
        //activeCamera.position = functions(0).offset
        //activeCamera.look(Math3D.addVectors(functions(0).offset, Math3D.addVectors(activeCamera.position, functions(0).a, 1), -1))
        //activeCamera
        Array(0)
      }
      override def render(canvas: BufferedImage): Array[Float] = {
        canvas.setRGB(0, 0, canvas.getWidth(), canvas.getHeight(), blank, 0, canvas.getWidth())
        try {
          ImageMod.drawQuickLine(canvas, activeCamera.projectToCanvas(canvas, activeCamera.project(Array(-20, 0, 10))), activeCamera.projectToCanvas(canvas, activeCamera.project(Array(20, 0, 10))), 255 << 16 | 255 << 8 | 255)
        } catch {
          case e: Exception =>
        }
        try {
          ImageMod.drawQuickLine(canvas, activeCamera.projectToCanvas(canvas, activeCamera.project(Array(0, -20, 10))), activeCamera.projectToCanvas(canvas, activeCamera.project(Array(0, 20, 10))), 255 << 16 | 255 << 8 | 255)
        } catch {
          case e: Exception =>
        }
        try {
          ImageMod.drawQuickLine(canvas, activeCamera.projectToCanvas(canvas, activeCamera.project(Array(0, 0, 15))), activeCamera.projectToCanvas(canvas, activeCamera.project(Array(0, 0, 5))), 255 << 16 | 255 << 8 | 255)
        } catch {
          case e: Exception =>
        }

        for(f <- functions) {

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

          points = points.sortWith((i: Array[Float], j: Array[Float]) => {
            Math3D.distanceBetweenSquared(activeCamera.position, i) > Math3D.distanceBetweenSquared(activeCamera.position, j)
          })
          /*points.foreach((i:Array[Float])=>{
        print(Math3D.distanceBetweenSquared(cam.position, i) + ", ")
        })
        println()*/

          var ColorC: Int = 0
          for (i <- points) {
            try {
              val point: Array[Int] = activeCamera.projectToCanvas(canvas, activeCamera.project(i))
              ImageMod.fillRect(canvas, point(0), point(1), 2, 2, 100 << 16 | ((500 - ColorC / 4) % 100) << 8 | (500 - ColorC / 4) % 255)
            } catch {
              case e: Exception =>
            }

            ColorC += 1
          }
          //things in the distance don't have to be ordered before they're rendered


          try {
            ImageMod.drawQuickLine(canvas, activeCamera.projectToCanvas(canvas, activeCamera.project(f.offset)), activeCamera.projectToCanvas(canvas, activeCamera.project(Math3D.addVectors(f.offset, f.a, 1))), 255 << 16 | 0 << 8 | 0)
          } catch {
            case e: Exception =>
          }
          try {
            ImageMod.drawQuickLine(canvas, activeCamera.projectToCanvas(canvas, activeCamera.project(f.offset)), activeCamera.projectToCanvas(canvas, activeCamera.project(Math3D.addVectors(f.offset, f.b, 1))), 0 << 16 | 255 << 8 | 0)
          } catch {
            case e: Exception =>
          }
          try {
            ImageMod.drawQuickLine(canvas, activeCamera.projectToCanvas(canvas, activeCamera.project(f.offset)), activeCamera.projectToCanvas(canvas, activeCamera.project(Math3D.addVectors(f.offset, f.c, 1))), 0 << 16 | 0 << 8 | 255)
          } catch {
            case e: Exception =>
          }
        }

        /*val c: Camera = new Camera()
        c.look(functions(0).offset)
        try {
          ImageMod.drawQuickLine(canvas, activeCamera.projectToCanvas(canvas, activeCamera.project(c.position)), activeCamera.projectToCanvas(canvas, activeCamera.project(Math3D.addVectors(c.position, Math3D.normalized(c.PN), 1))), 255 << 16 | 255 << 8 | 0)
          ImageMod.drawQuickLine(canvas, activeCamera.projectToCanvas(canvas, activeCamera.project(c.position)), activeCamera.projectToCanvas(canvas, activeCamera.project(Math3D.addVectors(c.position, c.NH, 1))), 0 << 16 | 255 << 8 | 255)
          ImageMod.drawQuickLine(canvas, activeCamera.projectToCanvas(canvas, activeCamera.project(c.position)), activeCamera.projectToCanvas(canvas, activeCamera.project(Math3D.addVectors(c.position, c.NV, 1))), 255 << 16 | 0 << 8 | 255)
        }catch{
          case e: Exception =>
        }*/

        Array(0)
      }
    }
    scene2 = new Scene3D(){
      this.activeCamera = new Camera()

      this.functions = Array(
        new Function3D(){
          override def function2D(s: Double): Array[Double] = {Array()}

          override def planeFunction(t: Double, u: Double): Array[Double] = {
            //Array(t, u, 0)
            Array(t, u, Math.cos(t)*Math.cos(t) + Math.sin(u)*Math.sin(u))
            //Array((t+1)*Math.cos(t), u, t)
            //Array(t, u, Math.sqrt(16 - t*t - u*u))//-2.5 to 2.5
            //Array(4*Math.cos(u)*Math.cos(t), 4*Math.cos(u)*Math.sin(t), 4*Math.sin(u))
          }
        }
      )
      val meshes: Array[Mesh] = new Array(functions.length)
      override def initialize(): Array[Float] = {
        for(i <- 0 until meshes.length){
          val iteration = 0.1f
          //meshes(i) = new Mesh(functions(i), Array(-Math.PI.toFloat, Math.PI.toFloat + iteration), Array(-Math.PI.toFloat/2, Math.PI.toFloat/2 + iteration), iteration, iteration)
            meshes(i) = new Mesh(functions(i), img, Array(0f, 10f), Array(0f, 10f), 0.2, 0.2)
        }
        Array(0)
      }

      //var y: Float = 0
      override def update(): Array[Float] = {
        /*functions(0) = new Function3D(){
          override def function2D(s: Double): Array[Double] = {Array()}

          override def planeFunction(t: Double, u: Double): Array[Double] = {
            //Array(Math.cos(t), Math.sin(u), t)
            Array(t, u, Math.cos(t+y)*Math.cos(t+y) + Math.sin(u+y) * Math.sin(u+y))
          }
        }
        for(i <- 0 until meshes.length) {
          meshes(i) = new Mesh(functions(i), img, Array(0, 10f), Array(0, 10f), 0.1, 0.1)
        }
        y=y+0.1f*/
        /*functions(0) = new Function3D(){
          override def function2D(s: Double): Array[Double] = {Array()}

          override def planeFunction(t: Double, u: Double): Array[Double] = {
            //Array(Math.cos(t), Math.sin(u), t)
            //Array(t, u, Math.sqrt(16 - t*t - u*u))//-2.5 to 2.5
            /*val f = Array(t, u, Math.cos((t+y)*3) * Math.cos((t+y)*3) + Math.sin((u+y)*3) * Math.sin((u+y)*3) + 0)
            Array(4*Math.cos(u)*Math.cos(t)*f(2), 4*Math.cos(u)*Math.sin(t)*f(2), 4*Math.sin(u)*f(2))*/
          }
        }
        for(i <- 0 until meshes.length){
          meshes(i) = new Mesh(functions(i), Array(-Math.PI.toFloat, Math.PI.toFloat + 0.1f), Array(-Math.PI.toFloat/2, Math.PI.toFloat/2 + 0.1f), 0.1, 0.1)
        }
        y=y+0.1f*/
        Array(0)
      }

      override def render(canvas: BufferedImage): Array[Float] = {
        canvas.setRGB(0, 0, canvas.getWidth(), canvas.getHeight(), blank, 0, canvas.getWidth())
        try {
          ImageMod.fillTriangle(canvas, activeCamera.projectToCanvas(canvas, activeCamera.project(mesh1.faces(0).A)),
            activeCamera.projectToCanvas(canvas, activeCamera.project(mesh1.faces(0).B)),
            activeCamera.projectToCanvas(canvas, activeCamera.project(mesh1.faces(0).C)),
            255 << 16 | 255 << 8 | 0)
          ImageMod.drawTriangle(canvas, activeCamera.projectToCanvas(canvas, activeCamera.project(mesh1.faces(0).A)),
                                        activeCamera.projectToCanvas(canvas, activeCamera.project(mesh1.faces(0).B)),
                                        activeCamera.projectToCanvas(canvas, activeCamera.project(mesh1.faces(0).C)),
                                  0 << 16 | 0 << 8 | 0, 1)
        }catch{case e: Exception => }
        for(mesh <- meshes){
          for(tr <- mesh.faces){
            try{
              /*ImageMod.fillTriangle(canvas, activeCamera.projectToCanvas(canvas, activeCamera.project(tr.A)),
                activeCamera.projectToCanvas(canvas, activeCamera.project(tr.B)),
                activeCamera.projectToCanvas(canvas, activeCamera.project(tr.C)),
                255 << 16 | 255 << 8 | (tr.A(2)*6 + 120).toInt%255)//255 << 16 | 150 << 8 | 70, 0.6)*/
              ImageMod.drawImageTri(tr.image, canvas, activeCamera.projectToCanvas(canvas, activeCamera.project(tr.A)),
                activeCamera.projectToCanvas(canvas, activeCamera.project(tr.B)),
                activeCamera.projectToCanvas(canvas, activeCamera.project(tr.C)),
                  activeCamera.projectToCanvas(canvas, activeCamera.project(tr.D)))
              /*ImageMod.drawTriangle(canvas, activeCamera.projectToCanvas(canvas, activeCamera.project(tr.A)),
                                            activeCamera.projectToCanvas(canvas, activeCamera.project(tr.B)),
                                            activeCamera.projectToCanvas(canvas, activeCamera.project(tr.C)),
                                      255 << 16 | 255 << 8 | 255, 5)*/
              //draw normal vectors
              /*ImageMod.drawQuickLine(canvas, activeCamera.projectToCanvas(canvas, activeCamera.project(tr.A)),
                                              activeCamera.projectToCanvas(canvas, activeCamera.project(Math3D.addVectors(tr.A, tr.N, 1))),
                                        255 << 16 | 255 << 8 | 0, 4)*/
            }catch{case e: Exception =>}
          }
        }
        Array(0)
      }
    }
    activeScene = scene2
    activeScene.initialize()
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

  val cubes: Array[Array[Array[Float]]] = Array(
    Objects3D.createCube(1, 0, -1, 1),
    Objects3D.createCube(5, 5, 1, 1),
    util.Objects3D.createCube(-2, 0, -1, 1),
    util.Objects3D.createCube(-5, 1, -1, 1))
  val boundingBox: Array[Array[Float]] = util.Objects3D.createCube(-15, -15, -15, 30)
  val point: Array[Float] = Array[Float](0f, 0f, 0f)
  val quad: Array[Array[Float]] = util.Objects3D.square3D(0, 0, 0, 5)
  val mesh1: Mesh = new Mesh(Array(new Trigon(Array(-10, -9, -11), Array(-11, -11, -10), Array(-9, -8, -9))))

  /*val f: Function3D = new Function3D { //convert to arc length parameterization
    offset = Array(0, 0, -20)
    a = Array(10, 0, 0)
    override def function2D(s: Double): Array[Double] = {
      Array(0.01*s, 100*s)
    }

    override def planeFunction(t: Double, u: Double): Array[Double] = {
      Array(
        /*10*Math.sin(t * Math.PI/180),
        10*Math.cos(t * Math.PI/180) * Math.cos(u * Math.PI/180),
        10*Math.cos(t * Math.PI/180) * Math.sin(u * Math.PI/180),*/
        t,
        2 * Math.cos(u),
        2 * Math.sin(u),
      )
    }
  }*/


  var angle: Float = 0;

  def update(): Unit = {
    activeScene.update()
    //cam.position(0)+=0.1f
    //println(cam.position(0))
    /*canvas.setRGB(0, 0, canvas.getWidth(), canvas.getHeight(), blank, 0, canvas.getWidth())

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
        ImageMod.fillRect(canvas, point(0), point(1), 2, 2, 100 << 16 | ((500 - ColorC/4)%100) << 8 | (500 - ColorC/4)%255)
      }catch{case e: Exception =>}

      ColorC+=1
    }
    //things in the distance don't have to be ordered before they're rendered

    try {
      ImageMod.drawQuickLine(canvas, cam.projectToCanvas(canvas, cam.project(f.offset)), cam.projectToCanvas(canvas, cam.project(Math3D.addVectors(f.offset, f.a, 1))), 255 << 16 | 0 << 8 | 0)
    } catch {
      case e: Exception =>
    }
    try {
      ImageMod.drawQuickLine(canvas, cam.projectToCanvas(canvas, cam.project(f.offset)), cam.projectToCanvas(canvas, cam.project(Math3D.addVectors(f.offset, f.b, 1))), 0 << 16 | 255 << 8 | 0)
    } catch {
      case e: Exception =>
    }
    try {
      ImageMod.drawQuickLine(canvas, cam.projectToCanvas(canvas, cam.project(f.offset)), cam.projectToCanvas(canvas, cam.project(Math3D.addVectors(f.offset, f.c, 1))), 0 << 16 | 0 << 8 | 255)
    } catch {
      case e: Exception =>
    }

    //f.a = Array(Math.cos(angle).toFloat, Math.sin(angle).toFloat, 0)
    f.a = Math3D.rotateAbout(f.a, Array(0, 1, 1.6f), 0.1)
    //f.b = Array(-Math.sin(angle).toFloat, Math.cos(angle).toFloat, 0)
    f.b = Math3D.rotateAbout(f.b, Array(0, 1, 1.6f), 0.1)
    f.b = Math3D.rotateAbout(f.b, f.a, 0.1)
    //f.c = Array(Math.sin(angle + Math.PI/2).toFloat, 0, Math.cos(angle + Math.PI/2).toFloat)
    f.c = Math3D.rotateAbout(f.c, Array(0, 1, 1.6f), 0.1)
    f.c = Math3D.rotateAbout(f.c, f.a, 0.1)

    //f.offset = Array(Math.cos(angle).toFloat, Math.sin(-angle).toFloat, 0)
    f.offset = Math3D.addVectors(f.offset, Math3D.scalarMult(f.a,10/Math3D.magnitude(f.a)), 1)
    angle+=0.1f

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
    }*/
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
    activeScene.render(canvas)
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
    val cam:Camera = activeScene.activeCamera
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
