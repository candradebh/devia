<template>
  <v-container>
    <!-- Caixa de Seleção para Projetos -->
    <v-row>
      <v-col cols="12" md="6">
        <v-select
          v-model="selectedProject"
          :items="projects"
          item-text="name"
          item-value="id"
          label="Selecione o Projeto"
          outlined
          dense
          @change="fetchChatHistory" 
        ></v-select>
      </v-col>
    </v-row>

    <!-- Histórico das conversas -->
    <v-row v-if="selectedProject">
      <v-col>
        <v-list dense>
          <v-list-item
            v-for="(message, index) in chatHistory"
            :key="index"
          >
            <v-list-item-content>
              <v-list-item-title v-if="message.sender === 'user'">
                <strong>Você:</strong> {{ message.message }}
              </v-list-item-title>
              <v-list-item-title v-else>
                <strong>Bot:</strong> {{ message.message }}
              </v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </v-list>
      </v-col>
    </v-row>

    <!-- Input de mensagem e botão enviar -->
    <v-row v-if="selectedProject" class="mt-5">
      <v-col>
        <v-textarea
          v-model="userMessage"
          label="Digite sua mensagem"
          outlined
          rows="2"
        ></v-textarea>
      </v-col>

      <v-col cols="auto">
        <v-btn color="primary" @click="sendMessage">Enviar</v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  name: "DeveloperChat",
  data() {
    return {
      projects: [],
      selectedProject: null,
      userMessage: "",
      chatHistory: [], // Histórico vai ser carregado do backend
    };
  },
  methods: {
    fetchProjects() {
      this.$api.get('/projects/isActive').then(response => {
        this.projects = response.data;
      });
    },
    fetchChatHistory() {
      if (this.selectedProject) {
        this.$api.get(`/chat/history/${this.selectedProject}`).then(response => {
          this.chatHistory = response.data;
        });
      }
    },
    sendMessage() {
      if (this.userMessage.trim() !== "") {
        const messageData = {
          projectId: this.selectedProject,
          message: this.userMessage,
        };
        //console.log(messageData);

        // Envia a mensagem ao backend
        this.$api.post('/chat/message?projectId='+this.selectedProject, messageData).then(response => {
          // Adiciona a mensagem do usuário no histórico
          this.chatHistory.push({
            sender: "user",
            message: this.userMessage,
          });

          // Adiciona a resposta do bot ao histórico
          this.chatHistory.push({
            sender: "bot",
            message: response.data,
          });

          // Limpa o campo de mensagem
          this.userMessage = "";
        });
      }
    },
  },
  created() {
    this.fetchProjects();
  },
};
</script>

<style scoped>
.v-list {
  height: 300px;
  overflow-y: auto;
  border: 1px solid #ddd;
  padding: 10px;
  border-radius: 8px;
}
</style>
