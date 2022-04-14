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
    while(t < 10){
      update()
      render(j)
      t+=1;
    }
    println("done")
  }

  val canvas: BufferedImage = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB)
  val blank: Array[Int] = new Array[Int](400 * 400)

  var img: BufferedImage = null

  val canvas_corners: Array[Array[Int]] = Array(
    Array(0, 0),
    Array(400, 0),
    Array(0, 400),
    Array(400, 400)
  )

  def init(): Unit ={
    try{
      img = ImageIO.read(new File("ref/rosie stays warm.jpg"))
    }catch{
      case e:Exception => println("couldn't load img")
    }
  }

  def update(): Unit ={
    canvas.setRGB(0, 0, 400, 400, blank, 0, 400)
    //ImageMod.drawImage(img, canvas, 0, 0, 400, 400)
    ImageMod.drawImage(img, canvas, canvas_corners(0), canvas_corners(1), canvas_corners(2), canvas_corners(3))
  }

  def render(j: JFrame): Unit ={
    val bs: BufferStrategy = j.getBufferStrategy()
    if(bs == null){
      j.createBufferStrategy(3)
      return
    }
    val g: Graphics = bs.getDrawGraphics()
    g.drawImage(canvas, 0, 0, 400, 400, null)
    bs.show()
  }

  @Override
  def mouseDragged(e: MouseEvent): Unit = {

  }

  var mousePos: Array[Int] = Array(400, 400)
  @Override
  def mouseMoved(e: MouseEvent): Unit = {
    mousePos(0) = e.getX
    mousePos(1) = e.getY
  }
}
