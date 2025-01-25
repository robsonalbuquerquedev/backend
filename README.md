
### 🛠️ Backend

O backend do projeto **ResourcesManagement** foi desenvolvido utilizando **Spring Boot**, garantindo robustez e escalabilidade na gestão de salas, laboratórios e recursos adicionais. Ele implementa as funcionalidades principais do sistema, como autenticação de usuários, gerenciamento de recursos e controle de permissões.

#### **Principais funcionalidades**  
- **Autenticação e Autorização**: Implementação de segurança com controle de acesso baseado no tipo de usuário (coordenador, professor ou aluno).  
- **Gerenciamento de Recursos**:  
  - Cadastro, edição e remoção de salas e laboratórios (exclusivo para coordenadores).  
  - Visualização e reserva de recursos para todos os usuários autenticados, com permissões dinâmicas.  
- **Controle de Permissões**:  
  - Coordenadores podem adicionar, editar e excluir recursos, além de realizar reservas.  
  - Professores podem realizar reservas, mas não têm acesso a edições ou exclusões.  
  - Alunos possuem funcionalidades limitadas, que ainda estão em desenvolvimento.  
  - Usuários deslogados têm acesso apenas à visualização de recursos disponíveis.  

#### **Estrutura de Pastas**
A organização do backend segue boas práticas de desenvolvimento, facilitando a manutenção e evolução do sistema:  

```plaintext
/backend  
│  
├── config/              # Configurações gerais do sistema e arquivos de segurança  
├── controller/          # Controladores responsáveis pelas APIs REST  
├── dto/                 # Objetos de transferência de dados para validação e comunicação  
├── exception/           # Tratamento e personalização de exceções  
├── infra/security/      # Configurações específicas de segurança (autenticação e autorização)  
├── model/               # Modelos das entidades do banco de dados  
├── repository/          # Interfaces para acesso ao banco de dados  
├── request/             # Modelos para requisições personalizadas  
├── service/             # Implementação das regras de negócio  
├── BackendApplication.java  # Classe principal da aplicação  
```  

#### **Tecnologias utilizadas**
- **Spring Boot**: Framework principal para o desenvolvimento do backend.  
- **Banco de Dados**: [Informe o banco de dados utilizado, ex.: MySQL, PostgreSQL, etc.].  
- **Segurança**: Implementação de autenticação e autorização com [ex.: Spring Security e JWT].  
- **API REST**: Endpoints organizados para facilitar a comunicação com o frontend em Vue.js.  

## Repositório Relacionado

Você também pode acessar o repositório [Frontend](https://github.com/rma98/resourcesManagement.git) para ver o código do cliente.
