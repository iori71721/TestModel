precision mediump float;
varying vec2 v_textureCoordinate;
uniform sampler2D u_texture;
void main() {
     vec2 color=v_textureCoordinate;
     if(color.x==0.0){
        color.x=1.0;
     }else{
        if(color.x==1.0){
            color.x=0.0;
        }
     }
     gl_FragColor = texture2D(u_texture, color);
}