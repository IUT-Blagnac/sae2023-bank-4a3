package application.tools;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.MultipleSelectionModel;

/**
 * Classe modèle de sélection utilisée pour faire que une ListView ne permette
 * pas de sélectionner une ligne.
 * La classe redéfinit les méthodes de MultipleSelectionModel.
 *
 * @param <T> Le type des objets de la ListView.
 * @author IUT Blagnac
 */
public class NoSelectionModel<T> extends MultipleSelectionModel<T> {

	/**
	 * @return ObservableList<Integer> Les indices des éléments sélectionnés.
	 * @author IUT Blagnac
	 */
	@Override
	public ObservableList<Integer> getSelectedIndices() {
		return FXCollections.emptyObservableList();
	}

	
	/** 
	 * @return ObservableList<T> Les éléments sélectionnés.
	 * @author IUT Blagnac
	 */
	@Override
	public ObservableList<T> getSelectedItems() {
		return FXCollections.emptyObservableList();
	}

	@Override
	public void selectIndices(int index, int... indices) {
	}

	@Override
	public void selectAll() {
	}

	@Override
	public void selectFirst() {
	}

	@Override
	public void selectLast() {
	}

	@Override
	public void clearAndSelect(int index) {
	}

	@Override
	public void select(int index) {
	}

	@Override
	public void select(T obj) {
	}

	@Override
	public void clearSelection(int index) {
	}

	@Override
	public void clearSelection() {
	}

	@Override
	public boolean isSelected(int index) {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public void selectPrevious() {
	}

	@Override
	public void selectNext() {
	}
}