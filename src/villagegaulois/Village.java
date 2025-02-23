package villagegaulois;

import personnages.Chef;
import personnages.Gaulois;

public class Village {
	private String nom;
	private Chef chef;
	private Gaulois[] villageois;
	private int nbVillageois = 0;
	private Marche marche;

	public Village(String nom, int nbVillageoisMaximum, int nbEtal) {
		this.nom = nom;
		villageois = new Gaulois[nbVillageoisMaximum];
		this.marche= new Marche(nbEtal);
	}

	public String getNom() {
		return nom;
	}

	public void setChef(Chef chef) {
		this.chef = chef;
	}

	public void ajouterHabitant(Gaulois gaulois) {
		if (nbVillageois < villageois.length) {
			villageois[nbVillageois] = gaulois;
			nbVillageois++;
		}
	}

	public Gaulois trouverHabitant(String nomGaulois) {
		if (nomGaulois.equals(chef.getNom())) {
			return chef;
		}
		for (int i = 0; i < nbVillageois; i++) {
			Gaulois gaulois = villageois[i];
			if (gaulois.getNom().equals(nomGaulois)) {
				return gaulois;
			}
		}
		return null;
	}

	public String afficherVillageois() {
		StringBuilder chaine = new StringBuilder();
		if (nbVillageois < 1) {
			chaine.append("Il n'y a encore aucun habitant au village du chef."
					+ chef.getNom() + ".\n");
		} else {
			chaine.append("Au village du chef " + chef.getNom()
					+ " vivent les légendaires gaulois :\n");
			for (int i = 0; i < nbVillageois; i++) {
				chaine.append("- " + villageois[i].getNom() + "\n");
			}
		}
		return chaine.toString();
	}
	public String installerVendeur(Gaulois vendeur, String produit,int nbProduit) {
		StringBuilder chaine = new StringBuilder();
		int EtalLibre = marche.trouverEtalLibre();
		chaine.append(vendeur.getNom()+" cherche un endroit pour vendre "+produit+". \n");
		if (EtalLibre!=-1) {
			marche.UtiliserEtal(EtalLibre, vendeur, produit, nbProduit);
			chaine.append("Le vendeur "+vendeur.getNom()+" vend des "+produit + " à l'étal n°"+(EtalLibre+1)+". \n");
		}else {
			chaine.append("Malheuresement tous les étals sont occupés \n");
		}
		return chaine.toString();
	}
	
	public String rechercherVendeursProduit(String produit) {
		StringBuilder chaine = new StringBuilder();
		Etal[] vendeurs = marche.trouverEtals(produit);
		if (vendeurs.length==0) {
			chaine.append("Il n'y a pas de vendeurs qui proposent des "+produit+ " au marché. \n");
		}else if(vendeurs.length==1) {
			chaine.append("Seul le vendeur "+vendeurs[0].getVendeur().getNom()+" propose des "+produit+" au marché. \n");
		} else {
			chaine.append("Les vendeurs qui proposent des "+produit+" sont: \n");
			for(int i=0;i<vendeurs.length;i++) {
				chaine.append("-"+vendeurs[i].getVendeur().getNom()+"\n");
			}
		}
		return chaine.toString();
	}

	public Etal rechercherEtal(Gaulois vendeur) {
		return marche.trouverVendeur(vendeur);
	}
	
	public String partirVendeur(Gaulois vendeur) {
		Etal libre = marche.trouverVendeur(vendeur);
		return libre.libererEtal();
	}
	
	public String afficherMarche() {
		StringBuilder chaine = new StringBuilder();
		chaine.append("Le marché du village "+ this.nom+ "possède plusieurs étals : \n");
		chaine.append(marche.afficherMarche());
		return chaine.toString();
	}
	
	private static class Marche {
		private Etal[] etals;
		
		public Marche(int nbEtal) {
			etals= new Etal[nbEtal];
			for (int i=0;i<nbEtal;i++) {
				etals[i]=new Etal();
			}
		}
		
		public void UtiliserEtal (int indiceEtal, Gaulois vendeur, String produit, int nbProduit){
			etals[indiceEtal].occuperEtal(vendeur, produit, nbProduit);
		}
		
		public int trouverEtalLibre() {
			int libre=-1;
			int i=0;
			while (libre==-1 && i<etals.length){
				if (etals[i].isEtalOccupe()==false) {
					libre=i;
				} else {
					i++;
				}
			}
			return libre;
		}
		
		public Etal[] trouverEtals(String produit) {
			Etal[] etalProduit;
			int count = 0;
			int p=0;
			for (int i=0;i<etals.length;i++) {
				if(etals[i].contientProduit(produit)) {
					count++;
				}
			}
			etalProduit= new Etal[count];
			for (int i=0;i<etals.length;i++) {
				if(etals[i].contientProduit(produit)) {
					etalProduit[p]=etals[i];
					p++;
				}
			}
			return etalProduit;
		}
		
		public Etal trouverVendeur(Gaulois gaulois) {
			for (int i=0; i <etals.length;i++) {
				if (etals[i].getVendeur()==gaulois) {
					return etals[i];
				}
			}
			return null;
		}
		
		public String afficherMarche() {
			StringBuilder chaine = new StringBuilder();
			int EtalVide=0;
			for (int i=0;i<etals.length;i++) {
				if (etals[i].isEtalOccupe()) {
					chaine.append(etals[i].afficherEtal());
				} else {
					EtalVide++;
				}
			}
			if(EtalVide!=0) {
				chaine.append("il reste "+EtalVide+" étals non utilisées dans le marché.\n");				
			}
			return chaine.toString();
			
		}
	}
}