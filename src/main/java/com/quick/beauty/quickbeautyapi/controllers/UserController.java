package com.quick.beauty.quickbeautyapi.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.quick.beauty.quickbeautyapi.entities.Admin;
import com.quick.beauty.quickbeautyapi.entities.Client;
import com.quick.beauty.quickbeautyapi.entities.Professional;
import com.quick.beauty.quickbeautyapi.entities.User;
import com.quick.beauty.quickbeautyapi.repositories.AdminRepository;
import com.quick.beauty.quickbeautyapi.repositories.ClientRepository;
import com.quick.beauty.quickbeautyapi.repositories.ProfessionalRepository;
import com.quick.beauty.quickbeautyapi.repositories.UserRepository;

import jakarta.annotation.security.RolesAllowed;

@RestController // Controlador Rest para controlar o envio das APIs solicitadas para trabalho
				// com dados
@RequestMapping(value = "/users") // Reuqest Mapping vai ouvir um determinado caminho da url para gerenciar as
									// requisições e responder
public class UserController {

	@Autowired // Vai criar uma injeção de dependência (ex.: new UserRepository) de forma
				// genérica para receber qualquer classe que retorne os dados equivalentes a
				// interface repository
	private UserRepository repoUser;
	
	@Autowired
	private AdminRepository repoAdmin;
	@Autowired
	private ProfessionalRepository repoProfessional;
	@Autowired
	private ClientRepository repoClient;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@RolesAllowed({ "ROLE_ADMIN" })
	@PutMapping(value = "/lockuser")
	@Transactional(propagation = Propagation.REQUIRED)
	public ResponseEntity<String> lockUser(@RequestParam String cpf){
		
		if( repoUser.cpfExists(cpf) > 0 ) {
			repoUser.lockUser(cpf);
			return ResponseEntity.status(HttpStatus.OK).body("Usuário bloqueado com sucesso!");
		
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado...");
		}
	}
	
	@RolesAllowed({ "ROLE_ADMIN" })
	@PutMapping(value = "/unlockuser")
	@Transactional(propagation = Propagation.REQUIRED)
	public ResponseEntity<String> unlockUser(@RequestParam String cpf){
		
		if( repoUser.cpfExists(cpf) > 0 ) {
			repoUser.unlockUser(cpf);
			return ResponseEntity.status(HttpStatus.OK).body("Usuário desbloqueado com sucesso!");
		
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado...");
		}
	}

	@RolesAllowed({ "ROLE_ADMIN", "ROLE_PROFESSIONAL" })
	@GetMapping // Informa que o verbo HTTP é o verbo GET (ou método HTTP)
	public ResponseEntity<?> findAll() {
		List<User> resultOfUsersQuery = repoUser.findAll();
		
		for(User currentUser : resultOfUsersQuery) {
			currentUser.setPassword(null);
			currentUser.setCpf(null);
			currentUser.setDateOfBirth(null);
		}
		
		return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(resultOfUsersQuery);
	}
	
	@RolesAllowed({ "ROLE_ADMIN" })
	@GetMapping(value = "/adminaccess") // Informa que o verbo HTTP é o verbo GET (ou método HTTP)
	public ResponseEntity<?> findAllAdminRole() {
		List<User> resultOfUsersQuery = repoUser.findAll();
		
		for(User currentUser : resultOfUsersQuery) {
			currentUser.setPassword(null);
		}
		
		return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(resultOfUsersQuery);
	}

	@RolesAllowed({ "ROLE_ADMIN" })
	@GetMapping(value = "/id/{id}") // O value e o Id vai servir para puxar um usuário através do Id na URL & o
									// PathVariable vai servir para puxar o Id da Url e jogar dentro do Id do
									// parâmetro do método e trabalhar com o valor
	public ResponseEntity<?> findUserById(@PathVariable Long id) {
		// Pelo fato do findById do repository retornar uma optional (um objeto especial
		// do java), e dentro desse objeto especial que vem o usuário com o Id
		// equivalente, é necessário usar o get()
		User resultOfUserQuery = repoUser.findById(id).get();
		resultOfUserQuery.setPassword(null);
		return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(resultOfUserQuery);
	}
	
	
	@RolesAllowed({ "ROLE_ADMIN", "ROLE_PROFESSIONAL" })
	@GetMapping(value = "/name")
	public ResponseEntity<?> findUserByName(@RequestParam("username") String username) {
		User resultOfUserQuery = repoUser.findByUsername(username);
		
		resultOfUserQuery.setPassword(null);
		resultOfUserQuery.setCpf(null);
		resultOfUserQuery.setDateOfBirth(null);
		
		return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(resultOfUserQuery);
	}
	
	@PostMapping(value= "getservices")
	public ResponseEntity<?> getServicesProfessionals(@RequestParam String specialty){
		List<Long> resultOfUsersIdsQuery = repoProfessional.getProfessionalService(specialty);
		List<User> resultOfUserQuery = new ArrayList<User>();
		
		if(resultOfUsersIdsQuery.size() > 0) {
			for(Long currentId : resultOfUsersIdsQuery) {
				resultOfUserQuery.add(repoUser.findByUserId(currentId));
			}
			
			for(User currentUser : resultOfUserQuery) {
				currentUser.setPassword(null);
				currentUser.setCpf(null);
				currentUser.setDateOfBirth(null);
			}
			
			return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(resultOfUserQuery);
		
		} else {
			return ResponseEntity.status(HttpStatusCode.valueOf(404)).body("Serviço não encontrado...");
		}
	}
	
	@GetMapping(value = "/email")
	public ResponseEntity<?> findAnotherUserInformations(@RequestParam("email") String email){
		User resultOfUserQuery = repoUser.findByEmail(email);
		
		resultOfUserQuery.setPassword(null);
		resultOfUserQuery.setCpf(null);
		resultOfUserQuery.setDateOfBirth(null);
		
		return ResponseEntity.status( HttpStatusCode.valueOf(200) ).body(resultOfUserQuery);
	}
	
	@GetMapping(value = "/myemail")
	public ResponseEntity<?> findCurrentUserInformations(@RequestParam("email") String email){
		User resultOfUserQuery = repoUser.findByEmail(email);
		resultOfUserQuery.setPassword(null);
		
		return ResponseEntity.status( HttpStatusCode.valueOf(200) ).body(resultOfUserQuery);
	}

	@RolesAllowed({ "ROLE_ADMIN" })
	@GetMapping(value = "/professional/id/{id}")
	public ResponseEntity<?> findProfessionalById(@PathVariable Long id) {
		Optional<User> userOptional = repoUser.findById(id);

		if (userOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
		}

		User user = userOptional.get();

		if ("PROFESSIONAL".equalsIgnoreCase(user.getType())) {
			user.setPassword(null);
			user.setCpf(null);
			user.setDateOfBirth(null);
			
			return ResponseEntity.status(HttpStatus.OK).body(user);
			
		} else if ( "CLIENT".equalsIgnoreCase(user.getType() ) || "ADMIN".equalsIgnoreCase(user.getType() ) ){
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body("Você não possui acesso para acessar as informações relacionadas ao usuário");
		
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Usuário procurado não existe...");
		}
	}
	
	@GetMapping(value = "/professional/name")
	public ResponseEntity<?> findProfessionalByUsername(@RequestParam String username) {
		Optional<User> userOptional = Optional.ofNullable(repoUser.findByUsername(username));

		if (userOptional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
		}

		User user = userOptional.get();

		if ("PROFESSIONAL".equalsIgnoreCase(user.getType())) {
			user.setPassword(null);
			user.setCpf(null);
			user.setDateOfBirth(null);
			
			return ResponseEntity.status(HttpStatus.OK).body(user);
			
		} else if ( "CLIENT".equalsIgnoreCase(user.getType() ) || "ADMIN".equalsIgnoreCase(user.getType() ) ){
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body("Você não possui acesso para acessar as informações relacionadas ao usuário");
		
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Usuário procurado não existe...");
		}
	}

	@PostMapping
	public ResponseEntity<String> saveNewUser(
			@RequestParam("username") String username,
			@RequestParam("cpf") String cpf,
			@RequestParam("dateOfBirth") String dateOfBirth,
			@RequestParam("email") String email,
			@RequestParam("password") String password,
			@RequestParam("type") String type,
			@RequestParam("phoneNumber") String phoneNumber,
			@RequestParam("cep") String cep,
			@RequestParam("houseNumber") String houseNumber,
			@RequestParam("evaluationNote") String evaluationNote,
			@RequestPart("userImage") MultipartFile userImage) {
		
		User newUser = new User(username, cpf, dateOfBirth, email, password, type,
				phoneNumber, cep, houseNumber, Double.parseDouble(evaluationNote) );
		
		
		if( newUser.cpfValidation(cpf) ) {
			// Encriptar Senha!
			String passwordEncoded = passwordEncoder.encode(newUser.getPassword());
			newUser.setPassword(passwordEncoded);
			newUser.setType( newUser.getType().toUpperCase() );
			newUser.setActive(true);
			
			if(!userImage.isEmpty()) {
				try {
					byte[] imageBytes = userImage.getBytes();
					
					String imageInBase64Encoded = Base64.encodeBase64String(imageBytes);
					
					newUser.setUserImage(imageInBase64Encoded);
				
				} catch( IOException ex ) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tivemos um prolema com a imagem enviada... " + ex.getMessage());
				}
			} else {
				newUser.setUserImage(null);
			}
	
			// Como o save do repository retorna uma referência do objeto (usuário) salvo,
			// eu posso pegar esse usuário que foi salvo e também retornar para mostrar os
			// dados salvos
			User resultOfUserSaved = repoUser.save(newUser);
	
			if (resultOfUserSaved.getType().equalsIgnoreCase("CLIENT")) {
				Client newClientUser = new Client();
				newClientUser.setUserId(resultOfUserSaved.getId());
	
				repoClient.save(newClientUser);
	
			} else if (resultOfUserSaved.getType().equalsIgnoreCase("PROFESSIONAL")) {
				Professional newProfessionalUser = new Professional();
				newProfessionalUser.setUserId(resultOfUserSaved.getId());
				newProfessionalUser.setSpecialty("NA");
	
				repoProfessional.save(newProfessionalUser);
	
			} else if (resultOfUserSaved.getType().equalsIgnoreCase("ADMIN")) {
				Admin newAdmin = new Admin();
				newAdmin.setUserId( resultOfUserSaved.getId() );
				newAdmin.setCriationDate( LocalDate.now() );
				
				repoAdmin.save(newAdmin);
				
			} else {
				Client newClientUser = new Client();
				newClientUser.setUserId(resultOfUserSaved.getId());
	
				repoClient.save(newClientUser);
			}
	
			return ResponseEntity.status(HttpStatusCode.valueOf(200)).body("Usuário Salvo com sucesso!");
		
		} else {
			return ResponseEntity.status(HttpStatusCode.valueOf(400)).body("CPF informado é inválido!");
		}
	}
	
	@PutMapping(value = "/update")
	@Transactional(propagation = Propagation.REQUIRED)
	public ResponseEntity<String> updateUserInformations(
			@RequestParam("username") String username,
			@RequestParam("cpf") String cpf,
			@RequestParam("dateOfBirth") String dateOfBirth,
			@RequestParam("email") String email,
			@RequestParam("phoneNumber") String phoneNumber,
			@RequestParam("cep") String cep,
			@RequestParam("houseNumber") String houseNumber){
		Integer statusCode = 0;
		String body = "";
		
		try {
			
			if( repoUser.cpfExists(cpf) > 0 ) {
				repoUser.updateUser(username, cpf, dateOfBirth, email, phoneNumber, cep, houseNumber);
				statusCode = 200;
				body = "Atualizações realizadas com sucesso!";
			
			} else {
				statusCode = 400;
				body = "Por gentileza, informe um CPF válido para alteração! O CPF não pode ser modificado! Por gentileza, entre em contato com o nosso time caso necessário.";
			}
			
		} catch(Exception ex) {
			statusCode = 500;
			body = "Não foi possível realizar a atualização dos dados solicitados - " + ex.getMessage();
		
		}
		
		return ResponseEntity.status(HttpStatusCode.valueOf(statusCode)).body(body);
	}
	
	@PutMapping(value = "/update/password")
	@Transactional(propagation = Propagation.REQUIRED)
	public ResponseEntity<String> updateUserPassword(
			@RequestParam("email") String email,
			@RequestParam("password") String password){
		
		Integer statusCode = 0;
		String body = "";
		
		if(repoUser.emailExists(email) > 0) {
			password = passwordEncoder.encode( password );
			repoUser.updatePassword(email, password);
			statusCode = 200;
			body = "Senha alterada com sucesso!";
			
		} else {
			statusCode = 400;
			body = "Usuário não encontrado...";
		}
		
		return ResponseEntity.status(statusCode).body(body);
	}
	
	@PutMapping(value = "/update/perfilimage")
	@Transactional(propagation = Propagation.REQUIRED)
	public ResponseEntity<String> updatePerfilImage(
			@RequestParam("email") String email,
			@RequestPart("userImage") MultipartFile userImage){
		
		Integer statusCode = 0;
		String body = "";
		
		if(repoUser.emailExists(email) > 0) {
			try{
				byte[] imageBytes = userImage.getBytes();
			
				String imageInBase64Encoded = Base64.encodeBase64String(imageBytes);
				repoUser.updateUserImage(email, imageInBase64Encoded);
				
				statusCode = 200;
				body = "Imagem alterada com sucesso!";
				
			} catch (IOException ex) {
				statusCode = 500;
				body = "Algo deu errado com o processamento da imagem... " + ex.getMessage();
			}
			
		} else {
			statusCode = 400;
			body = "Usuário não encontrado...";
		}
		
		return ResponseEntity.status(statusCode).body(body);
	}
	
	@PutMapping(value = "/update/usertype")
	@Transactional(propagation = Propagation.REQUIRED)
	public ResponseEntity<String> updateUserType(
			@RequestParam("email") String email,
			@RequestPart("type") String userType){
		
		Integer statusCode = 0;
		String body = "";
		
		if(repoUser.emailExists(email) > 0) {
			repoUser.updateUserType(email, userType.toUpperCase());
			
			statusCode = 200;
			body = "Typo de usuário alterado com sucesso!";
				
		} else {
			statusCode = 400;
			body = "Usuário não encontrado...";
		}
		
		return ResponseEntity.status(statusCode).body(body);
	}
	
	@PutMapping(value = "/update/evaluationnote")
	@Transactional(propagation = Propagation.REQUIRED)
	public ResponseEntity<String> updateUserEvaluationNote(
			@RequestParam("email") String email,
			@RequestParam("evaluationNote") String evaluationNote){
		
		Integer statusCode = 0;
		String body = "";
		
		if(repoUser.emailExists(email) > 0) {
			repoUser.updateUserEvaluationNote(email, Double.parseDouble(evaluationNote) );
			
			statusCode = 200;
			body = "Nota de avaliação do usuário ajustada com sucesso!";
				
		} else {
			statusCode = 400;
			body = "Usuário não encontrado...";
		}
		
		return ResponseEntity.status(statusCode).body(body);
	}

	@DeleteMapping("/{userId}")
	@Transactional(propagation = Propagation.REQUIRED)
	public ResponseEntity<String> deleteUser(@PathVariable Long userId) {

		Optional<User> userOptional = repoUser.findById(userId);

		if (userOptional.isPresent()) {
			repoProfessional.deleteUserById(userId);
			repoUser.deleteById(userId);

			return ResponseEntity.status(HttpStatusCode.valueOf(200)).body("Usuário Deletado com Êxito!");

		} else {
			return ResponseEntity.status(HttpStatusCode.valueOf(404)).body("Usuário não encontrado...");
		}
	}
	
	@GetMapping("/type/{userEmail}")
	public String getUserType(@PathVariable String userEmail) {
		Optional<User> userOptional = Optional.ofNullable(repoUser.findByEmail(userEmail));
		
		return userOptional.get().getType();
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body("Usuário não possui nível de autenticação para acesso dos dados...");
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<String> handleResourceNotFoundException(ConfigDataResourceNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Caminho solicitado não existe...");
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<String> handleUserAlreadyExistsException(DataIntegrityViolationException ex){
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Características do usuário informado já existem!");
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGenericException(Exception ex) {
		System.out.println("\n\n" + ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("Algo deu errado com o processamento da solicitação... " + ex.getMessage());
	}

}
