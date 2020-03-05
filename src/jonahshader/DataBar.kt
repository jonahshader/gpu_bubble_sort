package jonahshader

import processing.core.PApplet

class DataBar(originalIndex: Int, val value: Float, val width: Float) {
    var newIndex = originalIndex
    private var x = originalIndex.toFloat()

    fun draw(graphics: PApplet) {
        graphics.fill(255f, 255f, 255f)
        graphics.noStroke()
        graphics.rectMode(PApplet.CORNER)
        graphics.rect(x * width, (1-value) * graphics.height, width, value * graphics.height)
    }

    fun run() {
        x = .1f * newIndex + .9f * x
    }
}