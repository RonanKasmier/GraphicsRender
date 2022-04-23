package primitives

import util.Math3D

class Trigon(val A: Array[Float], val B: Array[Float], val C: Array[Float]) extends Face{
  val AB: Array[Float] = Math3D.addVectors(B, A, -1)
  val BC: Array[Float] = Math3D.addVectors(C, B, -1)
  val CA: Array[Float] = Math3D.addVectors(A, C, -1)
  val N: Array[Float] = Math3D.normalized(Math3D.crossProduct(AB, BC))
  val PAB: Array[Float] = Math3D.crossProduct(N, AB)
  val PBC: Array[Float] = Math3D.crossProduct(N, BC)
  val PCA: Array[Float] = Math3D.crossProduct(N, CA)

  val infront_of: List[Trigon] = null
  val behind: Set[Trigon] = null
  var should_render = true
  //var do_not_evaluate = false
  //                            HashMap<Int, Trigon>, <Trigon, Int>
  //Trigon, List[Trigon], Array[Tuple[int, Trigon]], Array[Tuple[List[Trigon], List[Trigon]]]
  //list of all faces, remove faces if occluded completely, those not infront nor behind stay on the list
  //consider table
  def createRenderGraph(trigons: List[Trigon]): List[Trigon] = {
    //var rootTrigon = trigons.head
    //list of not-placed trigons
    val highList: List[Trigon] = List[Trigon]()//for trigons that are not behind any Trigon
    createRenderGraphRecurse(trigons, highList)
  }
  def createRenderGraphRecurse(initialtrigons: List[Trigon], highList: List[Trigon]): List[Trigon] = {
    if(initialtrigons.head == null){
      return highList
    }
    val head = initialtrigons.head
    val trigons = initialtrigons.takeRight(1)
    for(trigon <- trigons){
      val depth = isInfrontOf(head, trigon)
    }

    if(head.behind.isEmpty) {
      createRenderGraphRecurse(trigons, head :: highList)
    }else{
      createRenderGraphRecurse(trigons, highList)
    }
  }

  def isInfrontOf(tri1: Trigon, tri2: Trigon): Int ={
    return 0
  }

  /**
   * @param startTri root face
   * @param func function of face
   * @param layers number of layers that should be evaluated
   */
  def traverseRenderGraph(trigons: List[Trigon], func: (Trigon => Unit), layers: Int): Unit = {
    for(tri <- trigons) {
      traverseRecurse(tri, func)
    }
  }
  def traverseRecurse(tri: Trigon, func: (Trigon => Unit)): Unit = {
      if (tri.infront_of == null || tri.infront_of.isEmpty) {
        func(tri)
        return
      }
      for (precede <- tri.infront_of) {
        traverseRecurse(precede, func)
      }
      func(tri)
  }

}
