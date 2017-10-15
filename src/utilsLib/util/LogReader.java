package utilsLib.util;

import com.Ostermiller.util.*;

import java.util.*;
import java.io.*;

import utilsLib.util.data.*;

/**
 * Leitura do log. O tempo de leitura é máxima para o limiar e é mais rápida a
 * medida que se aproxima das extremidades.
 */
public class LogReader {
	private String fileName;
	private String fileIDMask;
	private int threshold;

	public LogReader(String fileName, String fileIDMask) {
		setFileName(fileName);
		setFileIDMask(fileIDMask);
		setThreshold(500);
	}

	public LogReader() {
		setThreshold(500);
	}

	/**
	 * @param fileName
	 *            Nome do arquivo. Se for um nome dinanico, embutir a máscara
	 *            (fileIDMask). Por exemplo:
	 *            <code>c:\temp\log-<#data>.log</code>., onde
	 *            <code><#data></code> é a máscara.
	 * @param fileIDMask
	 *            Texto de máscara do arquivo.
	 * @param threshold
	 *            Existe a estrategia de leitura do log pelo fim ou pelo início.
	 *            O padrão é ler do começo, sendo que este parametro diz a
	 *            partir de quanto é para mudar de sentido. O padrão é 500.
	 */
	public LogReader(String fileName, String fileIDMask, int threshold) {
		setFileName(fileName);
		setFileIDMask(fileIDMask);
		setThreshold(threshold);
	}

	public int getLinesCount(String fileID) throws IOException {
		String filePath = null;
		if (fileName == null) {
			filePath = fileID;
		} else {
			filePath = Utils.replace(fileName, fileIDMask, fileID);
		}

		return Utils.getFileLinesCount(filePath);
	}

	/**
	 * Le do arquivo, já instanciando o que houver de acordo com a fábrica e a
	 * classe.
	 * 
	 * @param fileID
	 *            ID do arquivo. Se for nulo, pega o caminho padrão da class.
	 * @param filterEntity
	 *            Classe de filtro.
	 * @param pi
	 *            Paginação. Se for nulo, pega tudo.
	 * @param factory
	 *            Fábrica.
	 * @param classID
	 *            ID da classe da fábrica.
	 * @return Instancia de um array da clase especificada pelo classID.
	 * @throws IOException
	 */
	public Object[] read(String fileID, Entity filterEntity, PagingInfo pi,
			ObjFactory factory, int classID) throws IOException {
		if (factory == null) {
			throw new IllegalArgumentException("Param: factory");
		}

		Entity entity = (Entity) factory.getClassEntity(classID).clone();
		if (entity == null) {
			throw new IllegalArgumentException(
					"Param: classID. Não conrresponde a uma classe cadastrada na fábrica");
		}

		class Instanciator implements EntityListener {
			public ArrayList al = new ArrayList();
			public PagingInfo pi;
			public int curr;
			public ObjFactory factory;
			public int classID;

			public Instanciator(PagingInfo pi, ObjFactory factory, int classID) {
				this.pi = pi;
				this.factory = factory;
				this.classID = classID;
			}

			public boolean avail(Entity entity) {
				/** @todo testar isto! Ou seja, paginação com auto-instanciacao */
				if (pi != null) {
					int currPage = pi.getCurrPage();
					if (curr < (pi.getCurrPage() - 1) * pi.getPageSize()) {
						return true;
					} else if (curr > (pi.getCurrPage() - 1) * pi.getPageSize()) {
						return false;
					}
				}

				Entity classEntity = (Entity) factory.getClassEntity(classID)
						.clone();
				classEntity.copyEntityValues(entity);

				al.add(factory.paramDataToObj(new EntityParamDataInput(
						classEntity)));
				return true;
			}
		}

		Instanciator instanciator = new Instanciator(pi, factory, classID);
		read(fileID, entity, filterEntity, instanciator);

		return instanciator.al.toArray(factory.getArray(classID,
				instanciator.al.size()));
	}

	public Object[] read(ObjFactory factory, int classID) throws IOException {
		return read("", null, null, factory, classID);
	}

	public Object[] read(String fileID, ObjFactory factory, int classID)
			throws IOException {
		return read(fileID, null, null, factory, classID);
	}

	public Object[] read(String fileID, Entity filterEntity,
			ObjFactory factory, int classID) throws IOException {
		return read(fileID, filterEntity, null, factory, classID);
	}

	public void read(String fileID, Entity entity, Entity filterEntity,
			EntityListener listener) throws IOException {
		if (fileID == null) {
			throw new IllegalArgumentException("Parametro inválido: fileID");
		}

		if (entity == null) {
			throw new IllegalArgumentException("Parametro inválido: entity");
		}

		if (listener == null) {
			throw new IllegalArgumentException("Parametro inválido: listener");
		}

		String filePath = null;
		if (fileName == null) {
			filePath = fileID;
		} else {
			filePath = Utils.replace(fileName, fileIDMask, fileID);
		}

		InputStreamReader fos = new InputStreamReader(new FileInputStream(
				filePath), Utils.PATTERN_STRING_ENCODING);

		try {
			CSVParser csvP = new CSVParser(fos);
			csvP.setEscapes("nrtf", "\n\r\t\f");

			// leitura, mantendo o offset e o limit
			String[] line = csvP.getLine();
			while (line != null) {
				Entity lineEntity = (Entity) entity.clone();
				try {
					lineEntity.absorbFields(line);
				} catch (IllegalArgumentException error) {
					throw new IllegalArgumentException("Linha: "
							+ csvP.getLastLineNumber() + " - "
							+ error.getMessage());
				}

				// se não for similar, vai para a proxima linha, sem nem
				// avaliar.
				if (filterEntity == null || lineEntity.isSimilar(filterEntity)) {
					if (!listener.avail(lineEntity)) {
						break;
					}
				}

				line = csvP.getLine();
			}
		} finally {
			fos.close();
		}
	}

	public Entity[] read(String fileID, Entity entity, Entity filterEntity,
			PagingInfo pagingInfo, boolean ascending) throws IOException {
		if (pagingInfo == null) {
			throw new IllegalArgumentException("Parametro inválido: pagingInfo");
		}

		int offset = (pagingInfo.getCurrPage() - 1) * pagingInfo.getPageSize();
		int limit = pagingInfo.getPageSize();

		int count = pagingInfo.getRegsCount();
		if (count == Utils.UNDEFINED_NUMBER) {
			count = getLinesCount(fileID);
			pagingInfo.setRegsCount(count);
		}

		if (offset > count) {
			return new Entity[0];
		}

		if (offset < threshold || filterEntity != null) {
			EntityQuery eq = new EntityQuery(limit, offset, filterEntity,
					ascending);

			String filePath = null;
			if (fileName == null) {
				filePath = fileID;
			} else {
				filePath = Utils.replace(fileName, fileIDMask, fileID);
			}
			InputStreamReader fos = new InputStreamReader(new FileInputStream(
					filePath), Utils.PATTERN_STRING_ENCODING);
			try {
				CSVParser csvP = new CSVParser(fos);
				csvP.setEscapes("nrtf", "\n\r\t\f");
				LinkedList ll = new LinkedList();

				// leitura, mantendo o offset e o limit
				String[] line = csvP.getLine();

				while (line != null) {
					Entity lineEntity = (Entity) entity.clone();
					lineEntity.absorbFields(line);
					eq.avail(lineEntity);
					line = csvP.getLine();
				}
			} finally {
				fos.close();
			}

			Entity[] entities = eq.getEntities();

			return entities;
		} else {
			LinkedList al = new LinkedList();
			String filePath = null;
			if (fileName == null) {
				filePath = fileID;
			} else {
				filePath = Utils.replace(fileName, fileIDMask, fileID);
			}
			InputStreamReader fos = new InputStreamReader(new FileInputStream(
					filePath), Utils.PATTERN_STRING_ENCODING);
			try {
				CSVParser csvP = new CSVParser(fos);
				csvP.setEscapes("nrtf", "\n\r\t\f");

				// Posicionamento do ponteiro na posição correta. Se o offset
				// causar uma leitura menor que o limit, então configura isto.
				int readCount = limit;
				if (offset + limit < count) {
					// Caso seja em ordem acesdente, entao o ponteiro já está
					// na posição correta.
					if (!ascending) {
						for (int i = 0; i < count - (limit + offset); i++) {
							csvP.getLine();
						}
					} else {
						for (int i = 0; i < offset; i++) {
							csvP.getLine();
						}
					}
				} else {
					if (ascending) {
						for (int i = 0; i < offset; i++) {
							csvP.getLine();
						}
					} else {
						readCount = count - offset;
					}
				}

				// Le as entidades restantes.
				for (int i = readCount - 1; i >= 0; i--) {
					String[] line = csvP.getLine();
					if (line == null) {
						break;
					}
					Entity entityTemp = (Entity) entity.clone();
					// Povoamento da entidade.
					Field[] fields = entityTemp.getFieldsArray();
					for (int j = 0; j < fields.length; j++) {
						fields[j].setValue(line[j]);
					}

					// Se for ascendnete, acrescenta na ordem de leitura
					if (ascending) {
						al.add(entityTemp);
					} else {
						al.addFirst(entityTemp);
					}
				}
			} finally {
				fos.close();
			}

			return (Entity[]) al.toArray(new Entity[al.size()]);
		}
	}

	/**
	 * @todo realizar leitura com filtragem de forma otimizada. So é otimizado
	 *       quando não há filtragem. Isto consiste em inserir a otimização de
	 *       leitura dentro do algoritmo de leitura que obtem a quantidade geral
	 *       antes.
	 */
	public Entity[] read(String fileID, Entity entity, Entity filterEntity,
			int offset, int limit, boolean ascending) throws IOException {
		if (limit == 0) {
			return new Entity[0];
		}

		PagingInfo pi = new PagingInfo();
		pi.setCurrPage((offset / limit) + 1);
		pi.setPageSize(limit);
		pi.setRegsCount(Utils.UNDEFINED_NUMBER);
		return read(fileID, entity, filterEntity, pi, ascending);
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileIDMask() {
		return fileIDMask;
	}

	public void setFileIDMask(String fileIDMask) {
		if (fileIDMask == null) {
			fileIDMask = "";
		}
		this.fileIDMask = fileIDMask;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	/**
	 * Le tudo, apenas passando o separador. Isto ocorre em casos quando o
	 * separador é ;, e não ,.
	 * 
	 * @param separator
	 *            char
	 * @return String[][]
	 * @throws IOException
	 */
	public String[][] read(char separator) throws IOException {
		InputStreamReader fos = new InputStreamReader(new FileInputStream(
				fileName), Utils.PATTERN_STRING_ENCODING);

		try {
			CSVParser csvP = new CSVParser(fos);
			csvP.setEscapes("nrtf", "\n\r\t\f");

			try {
				csvP.changeDelimiter(separator);
			} catch (Exception error) {
			}

			// leitura, mantendo o offset e o limit
			return csvP.getAllValues();
		} finally {
			fos.close();
		}
	}

	public String[][] read() throws IOException {
		InputStreamReader fos = new InputStreamReader(new FileInputStream(
				fileName), Utils.PATTERN_STRING_ENCODING);

		try {
			CSVParser csvP = new CSVParser(fos);
			csvP.setEscapes("nrtf", "\n\r\t\f");

			// leitura, mantendo o offset e o limit
			return csvP.getAllValues();
		} finally {
			fos.close();
		}
	}

	public String[] readFirstLine() throws IOException {
		InputStreamReader fos = new InputStreamReader(new FileInputStream(
				fileName), Utils.PATTERN_STRING_ENCODING);

		try {
			CSVParser csvP = new CSVParser(fos);
			csvP.setEscapes("nrtf", "\n\r\t\f");

			// leitura, mantendo o offset e o limit
			return csvP.getLine();
		} finally {
			fos.close();
		}
	}
}
