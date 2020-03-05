package jonahshader

import jonahshader.opencl.CLFloatArray
import jonahshader.opencl.CLIntArray
import jonahshader.opencl.OpenCLProgram
import processing.core.PApplet

class App : PApplet() {
    companion object {
        const val NUM_DATA = 1280
    }

    lateinit var data: Data
    lateinit var clp: OpenCLProgram

    lateinit var indices: CLIntArray
    lateinit var values: CLFloatArray

    override fun settings() {
//        fullScreen()
        size(1280, 720)
        noSmooth()
    }

    override fun setup() {
        frameRate(165f)
        data = Data(NUM_DATA, this)
        clp = OpenCLProgram("sort_gpu.cl", arrayOf("sortAKernel", "sortBKernel"))

        indices = clp.createCLIntArray(NUM_DATA)
        values = clp.createCLFloatArray(NUM_DATA)

        for (i in 0 until NUM_DATA) {
            indices.array[i] = i
            values.array[i] = data.data[i].value
        }

        val sortAKernel = clp.getKernel("sortAKernel")
        var i = 0
        indices.registerAndSendArgument(sortAKernel, i++)
        values.registerAndSendArgument(sortAKernel, i++)

        val sortBKernel = clp.getKernel("sortBKernel")
        i = 0
        indices.registerAndSendArgument(sortBKernel, i++)
        values.registerAndSendArgument(sortBKernel, i++)

        indices.copyToDevice()
        values.copyToDevice()
    }

    override fun draw() {
        background(0)
        // run kernel code
//        if (frameCount % 6 == 0) {

        if (keyPressed) {
            for (i in 0 until NUM_DATA)
                values.array[i] += (Math.random() * 2 - 1).toFloat() * 0.01f
            values.copyToDevice()
        }

//        for (i in 1..1000) {
            clp.executeKernel("sortAKernel", NUM_DATA / 2L)
            clp.waitForCL()
            clp.executeKernel("sortBKernel", (NUM_DATA / 2L) - 1)
            clp.waitForCL()
//        }


            // copy indices back
//            indices.copyFromDevice()
            values.copyFromDevice()

            // update indices
//            data.updateIndices(indices.array)
//        }


//        data.update()
//        data.render(this)
        for (i in 0 until NUM_DATA) {
            rectMode(CORNER)
            fill(255f, 255f, 255f)
            noStroke()
            rect((i * (width / NUM_DATA.toFloat())).toInt().toFloat(), (1-values.array[i]) * height, max(width / NUM_DATA.toFloat(), 2f), values.array[i] * height)
        }
    }
}