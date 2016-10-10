all:    
	pdflatex -halt-on-error dynamic-chambers.tex > /dev/null
	pdflatex -halt-on-error fixed-chambers.tex > /dev/null
	pdflatex -halt-on-error traditional-list.tex > /dev/null
	pdflatex -halt-on-error rings.tex > /dev/null
	pdflatex -halt-on-error forward.tex > /dev/null
	pdflatex -halt-on-error backward.tex > /dev/null

once: 
	pdflatex -halt-on-error fixed-chambers.tex > /dev/null

alert: 
	pdflatex -halt-on-error fixed-chambers.tex

traditional: 
	pdflatex -halt-on-error traditional-list.tex > /dev/null

rings: 
	pdflatex -halt-on-error rings.tex > /dev/null

clean: 
	rm *.log *.nlo *.idx fixed-chambers.synctex* *.aux *.toc *.out ./misc/*.aux
