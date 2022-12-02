package norms;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class Organization {
    private Institution institution;
    private List<ConstitutiveNorm> constitutiveNorms;
}