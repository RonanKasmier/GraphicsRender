package tests

import javax.swing.JFrame
import java.awt.Graphics
import java.awt.image.{BufferStrategy, BufferedImage}
import java.awt.Color
import scala.io.BufferedSource
import util.ImageMod

import java.awt.event.{MouseEvent, MouseListener, MouseMotionListener}
import java.io.File
import javax.imageio.ImageIO

object ImageTest extends MouseMotionListener{

  def main(args: Array[String]): Unit ={
    val j: JFrame = new JFrame()
    j.setDefaultCloseOperation(3)
    j.setSize(400, 400)
    j.setResizable(false)
    j.setLocationRelativeTo(null)
    j.setVisible(true)
    j.addMouseMotionListener(this)

    init()

    var t: Long = 0
    var sart: Long = System.nanoTime()
    while(t < 100000){
      update()
      render(j)
      t+=1;
      println(1f/((System.nanoTime() - sart)/1_000_000_000.toDouble))
      sart = System.nanoTime()
    }
    println(1f/((System.nanoTime() - sart)/1_000_000_000.toDouble))
    println("done")
  }

  val canvas: BufferedImage = new BufferedImage(256/1, 224/1, BufferedImage.TYPE_INT_RGB)
  val blank: Array[Int] = new Array[Int](256*224/1)
  //val pixels: Array[Int] = canvas.getRGB(0, 0, 400, 400, null, 0, 400)

  var img: BufferedImage = null
  var img2: BufferedImage = null
  var img3: BufferedImage = null

  val canvas_corners: Array[Array[Int]] = Array(
    Array(0, 0),
    Array(256, 0),
    Array(0, 224),
    Array(256, 224)
  )

  def init(): Unit ={
    try{
      img = ImageIO.read(new File("ref/rosie stays warm.jpg"))
      //img2 = ImageMod.scaleImageSize(img, 80, 54)
      img3 = ImageMod.rotateImage(img, 180)
    }catch{
      case e:Exception => println("couldn't load img")
    }
  }

  //var degrees = 0
  def update(): Unit ={
    canvas.setRGB(0, 0, 256/1, 224/1, blank, 0, 256/1)
    //ImageMod.drawImage(img, canvas, 0, 0, 400, 400)
    //ImageMod.drawImage(img, canvas, canvas_corners(3), canvas_corners(2), canvas_corners(1), canvas_corners(0))
    //ImageMod.drawImageTri(img, canvas, canvas_corners(0), canvas_corners(1), canvas_corners(3), mousePos)
    ImageMod.drawImageTri(img, canvas, Array(0, 0), Array(100, 0), Array(100, 150), mousePos)
    //ImageMod.drawImageTri(img3, canvas, canvas_corners(3), canvas_corners(2), canvas_corners(0), mousePos)
    //img3 = ImageMod.rotateImage(img2, degrees)
    //degrees+=1
    //ImageMod.drawImage(img3, canvas, canvas_corners(0), canvas_corners(1), canvas_corners(2), canvas_corners(3))
    //println(img2)
    for(i <- 0 until 1) {
      //ImageMod.fillTriangle(canvas, Array[Int](75, 300), Array[Int](400, 350), mousePos, 0 << 16 | 0 << 8 | 0, 0.7f)
      //ImageMod.drawImage(img, canvas, canvas_corners(0), canvas_corners(1), canvas_corners(2), mousePos)
    }

  }

  def render(j: JFrame): Unit ={
    val bs: BufferStrategy = j.getBufferStrategy()
    if(bs == null){
      j.createBufferStrategy(3)
      return
    }
    val g: Graphics = bs.getDrawGraphics()
    //canvas.setRGB(0, 0, 400, 400, pixels, 0, 400);
    g.drawImage(canvas, 0, 0, 400, 400, null)

    bs.show()
  }

  @Override
  def mouseDragged(e: MouseEvent): Unit = {

  }

  var mousePos: Array[Int] = Array(400, 400)
  @Override
  def mouseMoved(e: MouseEvent): Unit = {
    mousePos(0) = (e.getX * 256/400f).toInt
    mousePos(1) = (e.getY*224/400f).toInt
  }
}
