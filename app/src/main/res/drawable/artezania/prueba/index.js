//operador rest => recibe un parametro y lo convierte en arreglo
function limpiarEspacios(cadenas) {
  let citas = {};
  let citas2 = [
    {
      id: "123",
      nombre: "lucas",
    },
  ];
  const objetopush = {
    id: "1234",
    nombre: "pedro",
  };
  console.log(cadenas);
  citas = [...citas, cadenas];
  /* 
    const l1 = [1,2,3]
    const l2 = [...l1, 4, 5]
    console.log(l1) // [1,2,3]
    console.log(l2) // [1,2,3,4,5]
  
  */
  console.log(citas);
  citas2.push(objetopush);
  console.log(citas2);
  return citas;
}
// let cadenasLimpias = limpiarEspacios("hola ", " algo ", " más");
const cadenasOriginales = {
  id: "12345",
  nombre: "mario",
};
//operador spread puede agregar un arreglo dentro de otro, ver index2
//o puede agregar un objeto dentro de un arreglo
//pero para eso tiene que estar dentro de llaves
/* 
({...cadenasOriginales}) === ({id:'12345',nombre:'mario'})
//esto no es posible agregar a un arreglo
(...cadenasOriginales) === (id:'12345',nombre:'mario')
*/
let cadenasLimpias = limpiarEspacios([cadenasOriginales]);
console.log(cadenasLimpias);
