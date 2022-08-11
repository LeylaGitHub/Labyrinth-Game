package cga.exercise.components.geometry

import cga.exercise.components.shader.ShaderProgram
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL30

/**
 * Creates a Mesh object from vertexdata, intexdata and a given set of vertex attributes
 *
 * @param vertexdata plain float array of vertex data
 * @param indexdata  index data
 * @param attributes vertex attributes contained in vertex data
 * @throws Exception If the creation of the required OpenGL objects fails, an exception is thrown
 *
 * Created by Fabian on 16.09.2017.
 */
class Mesh(vertexdata: FloatArray, indexdata: IntArray, attributes: Array<VertexAttribute>, private var material: Material? = null) {
    //private data
    private var vao = 0
    private var vbo = 0
    private var ibo = 0
    private var indexcount = 0

    init {
        //generate IDs & bind objects

        indexcount = indexdata.count()

        //vao
        val vaoID = GL30.glGenVertexArrays()
        vao = vaoID
        GL30.glBindVertexArray(vaoID)

        // vbo
        val vboID = GL30.glGenBuffers()
        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboID)
        GL30.glBufferData(GL30.GL_ARRAY_BUFFER, vertexdata, GL30.GL_STATIC_DRAW)

        // describe and activate VBO in the VAO
        // Alle Attribute in den Buffer laden
        for ((vertexAttributeIndex, vertexAttributeValues) in attributes.withIndex()) {
            // for attribute position of VAO:
            GL30.glEnableVertexAttribArray(vertexAttributeIndex)
            // describe its attribute:
            GL30.glVertexAttribPointer(
                vertexAttributeIndex, vertexAttributeValues.n, GL30.GL_FLOAT, false,
                vertexAttributeValues.stride, vertexAttributeValues.offset.toLong()
            )
        }

        // setup IBO //
        val iboID = GL30.glGenBuffers();
        GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, iboID);
        GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, indexdata, GL30.GL_STATIC_DRAW);
    }

    /**
     * renders the mesh
     */
    fun render() {
        // call the rendering method every frame
        GL30.glBindVertexArray(vao)
        GL30.glDrawElements(GL30.GL_TRIANGLES, indexcount, GL30.GL_UNSIGNED_INT, 0)
        GL30.glBindVertexArray(0)
    }

    /**
     * render with ShaderProgram as argument
     */
    fun render(shaderProgram: ShaderProgram) {
        // NOW LOAD UNIFORMS ONTO SHADER
        material?.bind(shaderProgram)
        render()
    }

    /**
     * Deletes the previously allocated OpenGL objects for this mesh
     */
    fun cleanup() {
        if (ibo != 0) GL15.glDeleteBuffers(ibo)
        if (vbo != 0) GL15.glDeleteBuffers(vbo)
        if (vao != 0) GL30.glDeleteVertexArrays(vao)
    }
}