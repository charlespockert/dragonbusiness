package io.github.charlespockert.data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.Sponge;

import io.github.charlespockert.entities.Company;

public class CompanyRepository {

	CompanyDao companyDao;

	public CompanyRepository() throws Exception {
		companyDao = Sponge.getServiceManager().getRegistration(CompanyDao.class).get().getProvider();
	}

	public boolean existsCompany(int id) throws SQLException {
		return companyDao.exists(id);
	}

	public boolean existsCompany(String name) throws SQLException {
		return companyDao.exists(name);
	}

	public Company getCompany(int uuid) throws SQLException {
		CompanyDto dto = companyDao.get(uuid);
		Company company = new Company();
		company.readFromDto(dto);		
		return company;
	}

	public Company getCompany(String name) throws SQLException {
		CompanyDto dto = companyDao.get(name);
		Company company = new Company();
		company.readFromDto(dto);		
		return company;
	}

	public List<Company> getByEmployeeId(String uuid) throws SQLException {
		ArrayList<Company> companies = new ArrayList<Company>();
		
		for(CompanyDto dto : companyDao.getByEmployeeId(uuid)) {
			Company company = new Company();
			company.readFromDto(dto);					
			companies.add(company);
		}
		
		return companies;
	}

	public List<Company> getAllCompanies() throws SQLException {
		ArrayList<Company> companies = new ArrayList<Company>();
		
		for(CompanyDto dto : companyDao.getAll()) {
			Company company = new Company();
			company.readFromDto(dto);					
			companies.add(company);
		}
		
		return companies;
	}

	public void createCompany(Company company) throws SQLException {
		CompanyDto  dto = company.writeToDto();
		companyDao.create(dto);
	}

	public void updateCompany(Company company) throws SQLException {
		CompanyDto dto = company.writeToDto();
		companyDao.update(dto);
	}

	public void deleteCompany(Company company) throws SQLException {
		CompanyDto dto = company.writeToDto();
		companyDao.delete(dto);
	}

}
