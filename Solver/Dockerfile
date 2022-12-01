FROM python:3.8-slim-buster

WORKDIR "/app"

COPY Solver.py .
COPY requirements.txt .
COPY TestData/ .

RUN  apt-get update \
  && apt-get install -y wget \
  && rm -rf /var/lib/apt/lists/

#Download compilers for minizinc
RUN wget https://github.com/MiniZinc/MiniZincIDE/releases/download/2.6.4/MiniZincIDE-2.6.4-bundle-linux-x86_64.tgz

RUN tar xf MiniZincIDE-2.6.4-bundle-linux-x86_64.tgz
ENV PATH=MiniZincIDE-2.6.4-bundle-linux-x86_64/bin:$PATH
ENV LD_LIBRARY_PATH=MiniZincIDE-2.6.4-bundle-linux-x86_64/lib:$LD_LIBRARY_PATH
ENV QT_PLUGIN_PATH=MiniZincIDE-2.6.4-bundle-linux-x86_64/plugins:$QT_PLUGIN_PATH

#Install minizinc python lib
RUN pip install -r requirements.txt

CMD python3 Solver.py