package cga.exercise.components.light

import cga.exercise.components.light.PointLight
import cga.exercise.components.shader.ShaderProgram
import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.*
import org.joml.Vector4f

open class SpotLight (
    var lightPosition: Vector3f,
    var lightColor: Vector3f,
    private var innerAngle: Float,
    private var outerAngle: Float,
    private var direction: Vector3f
):
    PointLight(lightPosition,lightColor), ISpotLight
{

    init {
        translate(lightPosition)
    }
    override fun bind(shaderProgram: ShaderProgram, viewMatrix: Matrix4f){
        var lightpos = Vector4f(getWorldPosition(), 1.0f).mul(viewMatrix)
        shaderProgram.setUniform("lightPosition", Vector3f(lightpos.x, lightpos.y, lightpos.z))
        shaderProgram.setUniform("lightColor", lightColor)
        shaderProgram.setUniform("innerAngle", Math.cos(innerAngle))
        shaderProgram.setUniform("outerAngle", Math.cos(outerAngle))
        shaderProgram.setUniform("direction", getWorldZAxis().negate().mul(Matrix3f(viewMatrix)))
    }
}