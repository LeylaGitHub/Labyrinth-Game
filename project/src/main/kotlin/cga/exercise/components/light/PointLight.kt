package cga.exercise.components.light


import cga.exercise.components.geometry.Transformable
import cga.exercise.components.shader.ShaderProgram
import org.joml.Vector3f

open class PointLight(
    var lightPosition: Vector3f,
    var lightColor: Vector3f
):
    IPointLight, Transformable() {

    init {
        translate(lightPosition)
    }

    override fun bind(shaderProgram: ShaderProgram){
        shaderProgram.setUniform("lightPosition", lightPosition)
        shaderProgram.setUniform("lightColor", lightColor)
    }
}