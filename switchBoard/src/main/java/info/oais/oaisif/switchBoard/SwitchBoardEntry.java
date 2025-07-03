
package info.oais.oaisif.switchBoard;

import java.io.Serializable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "oaisif-switchBoard")
public class SwitchBoardEntry implements Serializable {

    @Id
    private long id;
    private static final long serialVersionUID = 1L;

    private String archiveName;
    private String archiveDescription;
    private String archiveURL;

    public SwitchBoardEntry() {
        super();
    }

    @Override
    public String toString() {
        return "Name: " + archiveName + "\n" +
               "Description: " + archiveDescription + "\n" +
               "URL and Port: " + archiveURL + "\n";
    }

	public String getArchiveName() {
		return archiveName;
	}

	public long getId() {
		return id;
	}

	public String getArchiveDescription() {
		return archiveDescription;
	}

	public void setId(long timeMillis) {
		this.id = timeMillis;
	}

	public void setArchiveName(String string) {
		this.archiveName = string;
	}

	public void setArchiveDescription(String desc) {
		this.archiveDescription = desc;
	}

	public void setArchiveURL(String urlStr) {
		this.archiveURL = urlStr;
	}
}
