package br.edu.ifpe.manager.model;

public enum TipoUsuario {
    PROFESSOR("admin"),
    COORDENADOR("user"),
    ALUNO("user");
	
	private String role;
	
	TipoUsuario(String role){
		this.role = role;
	}
	
	public String getRole() {
		return role;
	}
}
