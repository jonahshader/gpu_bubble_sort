package jonahshader

import processing.core.PApplet

class Data(numData: Int, graphics: PApplet) {
    val data = mutableListOf<DataBar>()

    init {
        for (i in 0 until numData)
            data.add(DataBar(i, Math.random().toFloat(), graphics.width / numData.toFloat()))
    }

    fun render(graphics: PApplet) {
        for (d in data)
            d.draw(graphics)
    }

    fun update() {
        for (d in data)
            d.run()
    }

    fun updateIndices(indices: IntArray) {
        for (i in indices.indices) {
            data[i].newIndex = indices[i]
        }
    }
}