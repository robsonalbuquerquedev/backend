package br.edu.ifpe.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpe.manager.model.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}