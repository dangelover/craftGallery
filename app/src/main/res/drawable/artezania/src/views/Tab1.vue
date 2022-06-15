<template>
  <ion-page>
    <ion-header>
      <ion-toolbar>
        <ion-buttons slot="start">
          <ion-button >
            <ion-icon :icon="heart"></ion-icon>
          </ion-button>
        </ion-buttons>
        <ion-buttons slot="end">
          <ion-menu-button auto-hide="false"></ion-menu-button>
        </ion-buttons>
        <ion-title>Lab3-Ej1234-Kevin</ion-title>
      </ion-toolbar>
    </ion-header>
    <ion-content :fullscreen="true">
      <ion-button color="primary" @click="alertCancel()">
        <ion-icon slot="end" :icon="add"></ion-icon>
      </ion-button>
      <ion-list>
        <ion-list-header>Recent Conversations</ion-list-header>

        <ion-item v-for="item of usuarios" :key="item.id">
          <ion-label>
            <h2 v-text="item.id"></h2>

            <ion-button size="small" fill="clear" @click="alertShowUser(item.id, item.title, item.body)">

              <h3 v-text="item.title.slice(0,25)"  ></h3>
            </ion-button>
            <p v-text="item.body"></p>
          </ion-label>
          <div id="formatoBotones">
            <ion-button
              color="warning"
              slot="end"
              @click="alertEditUser(item.id, item.title, item.body)"
            >
              <ion-icon :icon="text"></ion-icon>
            </ion-button>
            <ion-button color="danger" slot="end" @click="alertDeleteUser(item.id)">
              <ion-icon :icon="trash"></ion-icon>
            </ion-button>
          </div>
        </ion-item>
      </ion-list>
     <ion-infinite-scroll
        @ionInfinite="loadData($event)" 
        threshold="100px" 
        id="infinite-scroll"
      >
        <ion-infinite-scroll-content
          loading-spinner="bubbles"
          loading-text="Loading more data...">
        </ion-infinite-scroll-content>
      </ion-infinite-scroll>
    </ion-content>
  </ion-page>
</template>

<script >
//importamos estos componentes para luego inyectarlos en define componentes, para luego definir que componetes usara vue
import {
  IonContent,
  IonHeader,
  IonPage,
  IonTitle,
  IonToolbar,
  IonButton,
  alertController,
  IonButtons,
  IonIcon,
  IonMenuButton,
  IonItem,
  IonListHeader,
  IonLabel,
  toastController,
  IonList,
  IonInfiniteScroll, 
  IonInfiniteScrollContent
} from "@ionic/vue";
import { defineComponent } from "vue";
import { add, heart, text, trash } from "ionicons/icons";

export default defineComponent({
  name: "Home",
  components: {
    IonContent,
    IonHeader,
    IonPage,
    IonTitle,
    IonToolbar,
    IonButton,
    IonButtons,
    IonIcon,
    IonMenuButton,
    IonItem,
    IonListHeader,
    IonLabel,
    IonList,
    IonInfiniteScroll, 
  IonInfiniteScrollContent,
  },
  setup() {
    
    return {
      add,
      heart,
      text,
      trash
    };
  },
  data() {
    return {
      usuarios: [],
      userId: 1,
      titulo:""
      
    };
  },
  methods: {
    async getData() {
      const request = await fetch(`https://jsonplaceholder.typicode.com/posts?userId=${this.userId}`);
      const response = await request.json();
      this.usuarios = await response;
      this.usuarios.forEach(element => {
        const titulo=element.title;
        const reduce = (text, length) => text.split(" ", length).join(" ")
        console.log((reduce(titulo,5)));
        
      });
          
      console.log(response);
    },
    
    loadData(event){

      
      this.userId++;
      setTimeout(()=>{
        
        this.getData();
        console.log("cargando siguientes");
        event.target.complete();

      },50);
      
      
      
      
      
      
    },
    async createdRow2(title, body) {
      const request = await fetch("https://jsonplaceholder.typicode.com/posts/", {
        method: "POST",
        body: JSON.stringify({
          id: 13,
          title: title,
          body: body
        }),
        headers: {
          "Content-type": "application/json; charset=UTF-8"
        }
      });
      if (request.ok) {
        const response = await request.json();
        const usuario = await response;
        this.usuarios.unshift(usuario);
        await console.log(usuario);
        console.log("Registros correctamente");
        this.openToast("Registros creado correctamente");
      }
    },
    async updateRow2(id, title, body) {
      const request = await fetch(
        "https://jsonplaceholder.typicode.com/todos/" + id,
        {
          //put envia todos los campos, patch: envia solo los campos modificados
          method: "PUT",
          body: JSON.stringify({
            id: id,
            title: title,
            body: body
          }),
          headers: {
            "Content-type": "application/json; charset=UTF-8"
          }
        }
      );

      if (request.ok) {
        const response = await request.json();
        const usuario = await response;
        //mediante map realizamos un mapeo para identificar el index
        const pos = this.usuarios
          .map(function(e) {
            //me devuelve el indexdel id que estamos pasando como parametro que ya tenemos identificados al resivir el parametro
            //ese id guardamos en la variable pos para que nos devuelva la posicion en la que se encuntra ese elemento en el array
            return e.id;
          })
          .indexOf(id);
        //desde la posicion tal agrega un elemento con el contenido de usuario, el contenido de usuario viene de lo que recibimos
        //como respuesta, y este mismo lo colocamos en el array correspondiente
        this.usuarios.splice(pos, 1, usuario);
        console.log("Registros Actualizados correctamentes");
        this.openToast("Registros Actualizados correctamentes");
      }
    },
    async deleteRow(id) {
      const response = await fetch("https://jsonplaceholder.typicode.com/todos/" + id, {
        method: "DELETE"
      });
      if (response.ok) {
        //obtenemos la posicion del id
        const pos = this.usuarios
          .map(function(e) {
            return e.id;
          })
          .indexOf(id);
        //eliminamos el id usando el  splice para indicarle la posicion y eliminar un elemento
        this.usuarios.splice(pos, 1);
        console.log("Eliminado Correctamente");
        this.openToast("Eliminado Correctamente");
      }
    },
    async alertCancel() {
      const alert = await alertController.create({
        cssClass: "my-custom-class",
        header: "Agregar Usuario",
        inputs: [
          {
            name: "titulo",
            id: "title",
            type: "text",
            placeholder: "Titulo"
          },
          {
            name: "body",
            id: "body",
            type: "text",
            placeholder: "Body"
          }
        ],
        buttons: [
          {
            text: "Cancelar",
            role: "cancel",
            cssClass: "secondary",
            handler: () => {
              console.log("Confirmado el cancelado");
            }
          },
          {
            text: "Aceptar",
            handler: data => {
              this.createdRow2(
                data.titulo,
                data.body
              );
            }
          }
        ]
      });
      return alert.present();
    },
    async alertShowUser(id, title, body) {
      const alert = await alertController
        .create({
          cssClass: 'my-custom-class',
          header: id,
          subHeader: title,
          message: body,
          buttons: ['OK'],
        });
      return alert.present();
    },
    async alertEditUser(id, title, body) {
      const alert = await alertController.create({
        cssClass: "my-custom-class",
        header: "Agregar Usuario",
        inputs: [
          {
            name: "id",
            id: "id",
            value: id,
            type: "text",
            disabled: true
          },
          {
            name: "titulo",
            id: "title",
            value: title,
            type: "text",
            placeholder: "Titulo"
          },
          {
            name: "body",
            id: "body",
            value: body,
            type: "text",
            placeholder: "Body"
          }
        ],
        buttons: [
          {
            text: "Cancelar",
            role: "cancel",
            cssClass: "secondary",
            handler: () => {
              console.log("Confirmado el cancelado");
            }
          },
          {
            text: "Aceptar",
            handler: data => {
              this.updateRow2(
                data.id,
                data.titulo,
                data.body
              );
            }
          }
        ]
      });
      return alert.present();
    },
    async alertDeleteUser(id) {
      const alert = await alertController.create({
        cssClass: "my-custom-class",
        header: "Eliminar Usuario",
        message: "¿Estas seguro de <strong>Eliminar el usuario</strong>?",
        buttons: [
          {
            text: "Cancel",
            role: "cancel",
            cssClass: "secondary",
            handler: blah => {
              console.log("Confirm Cancel:", blah);
            }
          },
          {
            text: "Aceptar",
            handler: () => {
              this.deleteRow(id);
              console.log("Confirm Okay");
            }
          }
        ]
      });
      return alert.present();
    },
    async openToast(mensaje) {
      const toast = await toastController.create({
        message: mensaje,
        duration: 2000
      });
      return toast.present();
    },
    
    
  },

  mounted() {
    this.getData();
  }
});
</script>

<style scoped>
#container {
  text-align: center;

  position: absolute;
  left: 0;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
}

#container strong {
  font-size: 20px;
  line-height: 26px;
}

#container p {
  font-size: 16px;
  line-height: 22px;

  color: #8c8c8c;

  margin: 0;
}
.titulo{
  text-align: center;
  color: #8c8c8c;
}
#formatoBotones{
  margin: 0;
  padding: 0;
}

#container a {
  text-decoration: none;
}
</style>